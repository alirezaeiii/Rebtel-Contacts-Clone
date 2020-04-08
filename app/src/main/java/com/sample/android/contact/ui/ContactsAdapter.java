package com.sample.android.contact.ui;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

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
    private boolean mShowSeparator;

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
            imageText.setText(contact.getBriefName());
            phoneNumberView.setText(number);

            flagItem.removeAllViews();
            for (int flagResId : contact.getFlagResIds()) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_width),
                        (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_height));
                params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_margin_end));
                imageView.setLayoutParams(params);
                imageView.setImageResource(flagResId);
                flagItem.addView(imageView);
            }

            ConstraintSet constraintSet = new ConstraintSet();
            int viewId;
            if (numbers.size() == 1) {
                phoneNumberType.setVisibility(View.VISIBLE);
                lineNumber.setVisibility(View.INVISIBLE);
                phoneNumberType.setText(phoneNumber.getTypeLabel());
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

            char[] nameArray = contact.getAccentName().toUpperCase().toCharArray();

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
                            char[] previousNameArray = previousContact.getAccentName().toUpperCase().toCharArray();

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
                            char[] nextNameArray = nextContact.getAccentName().toUpperCase().toCharArray();

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
                    char[] nextNameArray = nextContact.getAccentName().toUpperCase().toCharArray();

                    if ((Character.isLetter(nameArray[0]) || Character.isLetter(nextNameArray[0]))
                            && nameArray[0] != nextNameArray[0]) {
                        lineFlag = false;
                    }
                }
            }

            boolean expanded = contact.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            for (int childPosition = 0; childPosition < numbers.size(); childPosition++) {

                phoneNumber = numbers.get(childPosition);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = inflater.inflate(R.layout.child_item, null);

                ChildViewHolder childViewHolder = new ChildViewHolder(childView);

                childViewHolder.contactNumber.setText(phoneNumber.getNumber());
                childViewHolder.numberType.setText(phoneNumber.getTypeLabel());
                childViewHolder.flagImageView.setImageResource(phoneNumber.getFlagResId());

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

            if (contact.getPhoneNumbers().size() == 1) {
                return;
            }

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
