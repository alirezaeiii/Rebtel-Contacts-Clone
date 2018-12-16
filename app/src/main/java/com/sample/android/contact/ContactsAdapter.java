package com.sample.android.contact;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sylversky.indexablelistview.scroller.Indexer;
import com.sylversky.indexablelistview.section.AlphabetSection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sample.android.contact.Utils.deAccent;
import static com.sample.android.contact.Utils.getFlagImageView;
import static com.sample.android.contact.Utils.getFlagResID;
import static com.sample.android.contact.Utils.getNormalizedNumber;
import static com.sample.android.contact.Utils.getTypeValue;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Indexer {

    private List<Contact> mContacts;
    private RecyclerView mRecyclerView;
    private RecyclerView.SmoothScroller mSmoothScroller;

    // State of the row that needs to show separator
    private static final int SECTIONED_STATE = 1;
    // State of the row that need not show separator
    private static final int REGULAR_STATE = 2;
    // Cache row states based on positions
    private int[] mSeparatorRowStates;
    private int[] mLineRowStates;
    private AlphabetSection mAlphabetSection;
    private boolean mShowSeparator;

    ContactsAdapter() {
        mAlphabetSection = new AlphabetSection(this);
    }

    void setItems(List<Contact> contacts, boolean showSeparator) {
        mContacts = contacts;
        mShowSeparator = showSeparator;
        mSeparatorRowStates = new int[getItemCount()];
        mLineRowStates = new int[getItemCount()];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Contact contact = mContacts.get(position);

        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

        mSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 150f / displayMetrics.densityDpi;
            }
        };
    }

    @Override
    public String getComponentName(int position) {
        return mContacts.get(position).getName();
    }

    @Override
    public Object[] getSections() {
        return mAlphabetSection.getArraySections();
    }

    @Override
    public int getPositionForSection(int i) {
        return mAlphabetSection.getPositionForSection(i, getItemCount());
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail)
        ConstraintLayout detail;

        @BindView(R.id.contact_name)
        TextView contactNameView;

        @BindView(R.id.phone_number)
        TextView phoneNumberView;

        @BindView(R.id.phone_type)
        TextView phoneNumberType;

        @BindView(R.id.line_number)
        TextView lineNumber;

        @BindView(R.id.subItem)
        LinearLayout subItem;

        @BindView(R.id.image_text)
        TextView imageText;

        @BindView(R.id.separator)
        View separatorView;

        @BindView(R.id.separator_text)
        TextView separatorText;

        @BindView(R.id.line)
        View line;

        @BindView(R.id.flagItem)
        LinearLayout flagItem;

        @BindView(R.id.flagImageView)
        ImageView flagImageView;

        private ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        private void bind(Contact contact) {

            Context context = mRecyclerView.getContext();
            final int position = getAdapterPosition();

            String name = contact.getName();
            List<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
            ContactPhoneNumber phoneNumber = numbers.get(0);
            String number = numbers.size() == 1 ? phoneNumber.getNumber() : "";

            contactNameView.setText(name);
            CountryCodeNumber countryCodeNumber = getNormalizedNumber(number);
            phoneNumberView.setText(countryCodeNumber.number);

            flagItem.removeAllViews();

            flagImageView.setImageResource(numbers.size() == 1 ?
                    getFlagResID(context, countryCodeNumber.regionCode) :
                    android.R.color.transparent);

            ConstraintSet constraintSet = new ConstraintSet();
            int viewId;
            if (numbers.size() == 1) {
                phoneNumberType.setVisibility(View.VISIBLE);
                lineNumber.setVisibility(View.INVISIBLE);
                phoneNumberType.setText(getTypeValue(phoneNumber));
                constraintSet.clone(detail);
                viewId = R.id.phone_type;
            } else {
                lineNumber.setVisibility(View.VISIBLE);
                phoneNumberType.setVisibility(View.INVISIBLE);
                lineNumber.setText(String.valueOf(numbers.size()));
                constraintSet.clone(detail);
                viewId = R.id.line_number;
            }
            constraintSet.connect(R.id.contact_name,
                    ConstraintSet.END,
                    viewId,
                    ConstraintSet.START);
            constraintSet.applyTo(detail);

            String[] splitedName = name.split("\\s+");
            char c;
            int i;
            boolean noLetter = true;

            for (i = 0; i < splitedName.length; i++) {
                c = splitedName[i].toUpperCase().charAt(0);
                if (Character.isLetter(c)) {
                    imageText.setText(String.valueOf(c));
                    noLetter = false;
                    break;
                }
            }

            for (int j = i + 1; j < splitedName.length; j++) {
                c = splitedName[j].toUpperCase().charAt(0);
                if (Character.isLetter(c)) {
                    imageText.append("." + c);
                    break;
                }
            }

            if (noLetter) {
                imageText.setText("¿");
            }

            char[] nameArray = deAccent(name).toUpperCase().toCharArray();

            boolean showSeparator = false;

            if (mShowSeparator) {

                // Show index separator ?
                switch (mSeparatorRowStates[position]) {

                    case SECTIONED_STATE:
                        showSeparator = true;
                        break;

                    case REGULAR_STATE:
                        showSeparator = false;
                        break;

                    default:

                        if (position == 0) {
                            showSeparator = true;
                        } else {
                            Contact previousContact = mContacts.get(position - 1);

                            String previousName = deAccent(previousContact.getName());
                            char[] previousNameArray = previousName.toUpperCase().toCharArray();

                            if (Character.isLetter(nameArray[0]) &&
                                    nameArray[0] != previousNameArray[0]) {
                                showSeparator = true;
                            }
                        }

                        // Cache it
                        mSeparatorRowStates[position] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;

                        break;
                }
            }

            if (showSeparator) {
                char ch = name.toUpperCase().charAt(0);
                if (Character.isLetter(ch)) {
                    separatorText.setText(name.toCharArray(), 0, 1);
                } else {
                    separatorText.setText("&");
                }
                separatorView.setVisibility(View.VISIBLE);
            } else {
                separatorView.setVisibility(View.GONE);
            }

            boolean showLine = true;

            if (mShowSeparator) {

                // Show line separator ?
                switch (mLineRowStates[position]) {

                    case SECTIONED_STATE:
                        showLine = false;
                        break;

                    case REGULAR_STATE:
                        showLine = true;
                        break;

                    default:

                        if (position == mContacts.size() - 1) {
                            showLine = false;
                        } else {
                            Contact nextContact = mContacts.get(position + 1);

                            String nextName = deAccent(nextContact.getName());
                            char[] nextNameArray = nextName.toUpperCase().toCharArray();

                            if ((Character.isLetter(nameArray[0]) && nameArray[0] != nextNameArray[0]) ||
                                    (!Character.isLetter(nameArray[0]) && Character.isLetter(nextNameArray[0])
                                            && nameArray[0] != nextNameArray[0])) {
                                showLine = false;
                            }
                        }

                        // Cache it
                        mLineRowStates[position] = showLine ? REGULAR_STATE : SECTIONED_STATE;

                        break;
                }

            }

            line.setVisibility(showLine ? View.VISIBLE : View.GONE);

            subItem.removeAllViews();

            if (numbers.size() == 1) {
                return;
            }

            boolean lineFlag = true;

            if (mShowSeparator) {

                if (position == mContacts.size() - 1) {
                    lineFlag = false;
                } else {
                    Contact nextContact = mContacts.get(position + 1);

                    String nextName = deAccent(nextContact.getName());
                    char[] nextNameArray = nextName.toUpperCase().toCharArray();

                    if ((Character.isLetter(nameArray[0]) || Character.isLetter(nextNameArray[0]))
                            && nameArray[0] != nextNameArray[0]) {
                        lineFlag = false;
                    }
                }
            }

            boolean expanded = contact.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            List<ImageView> list = new ArrayList<>();

            for (int childPosition = 0; childPosition < numbers.size(); childPosition++) {

                phoneNumber = numbers.get(childPosition);

                countryCodeNumber = getNormalizedNumber(phoneNumber.getNumber());
                ImageView imageView = getFlagImageView(context, countryCodeNumber);
                if (!list.contains(imageView)) {
                    flagItem.addView(imageView);
                    list.add(imageView);
                }

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = inflater.inflate(R.layout.child_item, null);

                ChildViewHolder childViewHolder = new ChildViewHolder(childView);

                childViewHolder.contactNumber.setText(getNormalizedNumber(phoneNumber.getNumber()).number);
                childViewHolder.numberType.setText(getTypeValue(phoneNumber));
                childViewHolder.flagImageView.setImageResource(getFlagResID(context, countryCodeNumber.regionCode));

                childViewHolder.childLine.setVisibility(lineFlag ? View.VISIBLE : View.GONE);
                childViewHolder.childTopLine.setVisibility(lineFlag ? View.GONE : View.VISIBLE);

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);

                FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (lineFlag) {
                    lp.setMarginStart((int) context.getResources().getDimension(R.dimen.dimen_frame_margin_default));
                    childViewHolder.frameLayout.setLayoutParams(lp);

                    rlp.setMarginStart((int) context.getResources().getDimension(R.dimen.dimen_relative_margin_default));
                    childViewHolder.relativeLayout.setLayoutParams(rlp);
                } else {

                    if (childPosition == 0) {
                        lp.setMarginStart((int) context.getResources().getDimension(R.dimen.dimen_frame_margin_default));
                        rlp.setMarginStart((int) context.getResources().getDimension(R.dimen.dimen_relative_margin_default));

                    } else {

                        lp.setMarginStart((int) context.getResources().getDimension(R.dimen.dimen_frame_margin));
                        rlp.setMarginStart(0);
                    }
                    childViewHolder.frameLayout.setLayoutParams(lp);
                    childViewHolder.relativeLayout.setLayoutParams(rlp);
                }

                subItem.addView(childView);
            }
        }

        @OnClick(R.id.detail)
        void onClick() {
            final Contact contact = mContacts.get(getAdapterPosition());

            boolean expanded = contact.isExpanded();
            contact.setExpanded(!expanded);
            notifyItemChanged(getAdapterPosition());

            if (contact.isExpanded()) {
                new Handler().postDelayed(() -> {
                    mSmoothScroller.setTargetPosition(getAdapterPosition());
                    mRecyclerView.getLayoutManager().startSmoothScroll(mSmoothScroller);
                }, 100);
            }

        }

        class ChildViewHolder {

            @BindView(R.id.contact_number)
            TextView contactNumber;

            @BindView(R.id.type)
            TextView numberType;

            @BindView(R.id.child_line)
            View childLine;

            @BindView(R.id.child_top_line)
            View childTopLine;

            @BindView(R.id.frameLayout)
            View frameLayout;

            @BindView(R.id.relativeLayout)
            View relativeLayout;

            @BindView(R.id.flagImageView)
            ImageView flagImageView;

            private ChildViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
