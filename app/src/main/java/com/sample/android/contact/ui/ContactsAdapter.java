package com.sample.android.contact.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_CONTACT = 2;
    private static final int TYPE_CONTACT_MULTIPLE = 3;
    private List<ContactItem> mContacts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.SmoothScroller mSmoothScroller;
    private boolean mShowSeparator;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_CONTACT: {
                View view = layoutInflater
                        .inflate(R.layout.contact_item, parent, false);
                return new ContactViewHolder(view);
            }
            case TYPE_SEPARATOR: {
                View view = layoutInflater
                        .inflate(R.layout.contact_separator, parent, false);
                return new SeparatorViewHolder(view);
            }
            case TYPE_CONTACT_MULTIPLE: {
                View view = layoutInflater
                        .inflate(R.layout.contact_multiple_items, parent, false);
                return new ContactMultipleViewHolder(view);
            }
            default:
                throw new RuntimeException("You must supply a valid type for this adapter");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ContactItem contactItem = mContacts.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_CONTACT:
                ((ContactViewHolder) holder).bind(contactItem.getContact());
                break;
            case TYPE_SEPARATOR:
                ((SeparatorViewHolder) holder).bind(contactItem.getContactSeparator());
                break;
            case TYPE_CONTACT_MULTIPLE:
                ((ContactMultipleViewHolder) holder).bind(contactItem.getContact());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ContactItem contactItem = mContacts.get(position);
        if (contactItem.getContactSeparator() == null) {
            if (contactItem.getContact().getPhoneNumbers().size() == 1) {
                return TYPE_CONTACT;
            }
            return TYPE_CONTACT_MULTIPLE;
        }
        return TYPE_SEPARATOR;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
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

    public void setItems(List<ContactItem> contacts, boolean showSeparator) {
        mContacts = contacts;
        mShowSeparator = showSeparator;
        notifyDataSetChanged();
    }

    class SeparatorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.separator)
        View separatorView;

        @BindView(R.id.separator_text)
        TextView separatorText;

        public SeparatorViewHolder(@NonNull View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        void bind(char contactSeparator) {
            if (mShowSeparator) {
                separatorText.setText(String.valueOf(contactSeparator));
                separatorView.setVisibility(View.VISIBLE);
            } else {
                separatorView.setVisibility(View.GONE);
            }
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contact_name)
        TextView contactNameView;

        @BindView(R.id.phone_number)
        TextView phoneNumberView;

        @BindView(R.id.phone_type)
        TextView phoneNumberType;

        @BindView(R.id.flagItem)
        ImageView flagImageView;

        @BindView(R.id.image_text)
        TextView imageText;

        @BindView(R.id.bottomLine)
        View bottomLine;

        public ContactViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        void bind(Contact contact) {
            Iterator<Integer> flags = contact.getFlagResIds().iterator();
            flagImageView.setImageResource(flags.next());
            visibleGone(bottomLine, contact.getShowBottomLine());
            Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
            Iterator<ContactPhoneNumber> iterator = numbers.iterator();
            ContactPhoneNumber phoneNumber = iterator.next();
            phoneNumberType.setText(phoneNumber.getTypeLabel());
            contactNameView.setText(contact.getName());
            imageText.setText(contact.getBriefName());
            phoneNumberView.setText(phoneNumber.getNumber());
        }
    }

    class ContactMultipleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contact_name)
        TextView contactNameView;

        @BindView(R.id.line_number)
        TextView lineNumber;

        @BindView(R.id.subItem)
        LinearLayout subItem;

        @BindView(R.id.image_text)
        TextView imageText;

        @BindView(R.id.bottomLine)
        View bottomLine;

        @BindView(R.id.flagItem)
        LinearLayout flagItem;

        public ContactMultipleViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        void bind(Contact contact) {
            Context context = mRecyclerView.getContext();
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
            visibleGone(bottomLine, contact.getShowBottomLine());
            subItem.removeAllViews();
            Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
            lineNumber.setText(String.valueOf(numbers.size()));
            contactNameView.setText(contact.getName());
            imageText.setText(contact.getBriefName());
            subItem.setVisibility(contact.isExpanded() ? View.VISIBLE : View.GONE);
            boolean showChildBottomLine = !mShowSeparator || contact.getShowChildBottomLine();
            for (ContactPhoneNumber phoneNumber : numbers) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = inflater.inflate(R.layout.contact_child_item, null);

                ChildViewHolder childViewHolder = new ChildViewHolder(childView);
                childViewHolder.contactNumber.setText(phoneNumber.getNumber());
                childViewHolder.numberType.setText(phoneNumber.getTypeLabel());
                childViewHolder.flagImageView.setImageResource(phoneNumber.getFlagResId());

                childViewHolder.childBottomLine.setVisibility(showChildBottomLine ? View.VISIBLE : View.GONE);
                childViewHolder.childTopLine.setVisibility(showChildBottomLine ? View.GONE : View.VISIBLE);

                FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        (int) context.getResources().getDimension(R.dimen.dimen_child_contact_item_height));

                rlp.setMarginStart(phoneNumber.getStartMargin());
                childViewHolder.relativeLayout.setLayoutParams(rlp);
                childViewHolder.frameLayout.setPadding(phoneNumber.getStartPadding(), 0, 0, 0);
                subItem.addView(childView);
            }
        }

        @OnClick(R.id.detail)
        void onClick() {
            final Contact contact = mContacts.get(getAdapterPosition()).getContact();

            boolean expanded = contact.isExpanded();
            contact.setExpanded(!expanded);
            notifyItemChanged(getAdapterPosition());

            if (contact.isExpanded()) {
                mHandler.postDelayed(() -> {
                    mSmoothScroller.setTargetPosition(getAdapterPosition());
                    mRecyclerView.getLayoutManager().startSmoothScroll(mSmoothScroller);
                }, 100);
            }
        }
    }

    private void visibleGone(View view, boolean showBottomLine) {
        if (mShowSeparator) {
            view.setVisibility(showBottomLine ? View.VISIBLE : View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    static class ChildViewHolder {

        @BindView(R.id.contact_number)
        TextView contactNumber;

        @BindView(R.id.type)
        TextView numberType;

        @BindView(R.id.child_bottom_line)
        View childBottomLine;

        @BindView(R.id.child_top_line)
        View childTopLine;

        @BindView(R.id.frameLayout)
        View frameLayout;

        @BindView(R.id.relativeLayout)
        View relativeLayout;

        @BindView(R.id.flagImageView)
        ImageView flagImageView;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
