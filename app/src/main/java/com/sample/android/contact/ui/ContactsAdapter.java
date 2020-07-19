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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.domain.ContactSeparator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> mContacts;
    private RecyclerView mRecyclerView;
    private RecyclerView.SmoothScroller mSmoothScroller;
    private boolean mShowSeparator;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ConstraintSet mConstraintSet = new ConstraintSet();

    void setItems(List<Contact> contacts, boolean showSeparator) {
        mContacts = contacts;
        mShowSeparator = showSeparator;
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

        ViewHolder(View root) {
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

            if (mShowSeparator) {
                ContactSeparator contactSeparator = contact.getContactSeparator();
                if (contactSeparator != null && contactSeparator.getShowSeparator()) {
                    separatorText.setText(String.valueOf(contactSeparator.getSeparatorChar()));
                    separatorView.setVisibility(View.VISIBLE);
                } else {
                    separatorView.setVisibility(View.GONE);
                }
                line.setVisibility(contact.getShowLine() ? View.VISIBLE : View.GONE);
            } else {
                separatorView.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
            }
            subItem.removeAllViews();
            String name = contact.getName();
            List<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
            String number = "";
            int viewId;
            if (numbers.size() == 1) {
                ContactPhoneNumber phoneNumber = numbers.get(0);
                number = phoneNumber.getNumber();
                phoneNumberType.setVisibility(View.VISIBLE);
                lineNumber.setVisibility(View.INVISIBLE);
                phoneNumberType.setText(phoneNumber.getTypeLabel());
                viewId = R.id.phone_type;
            } else {
                lineNumber.setVisibility(View.VISIBLE);
                phoneNumberType.setVisibility(View.INVISIBLE);
                lineNumber.setText(String.valueOf(numbers.size()));
                viewId = R.id.line_number;
                boolean expanded = contact.isExpanded();
                subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
                boolean lineFlag = mShowSeparator ? contact.getLineFlag() : true;

                for (int childPosition = 0; childPosition < numbers.size(); childPosition++) {

                    ContactPhoneNumber phoneNumber = numbers.get(childPosition);

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

                    lp.setMarginStart(phoneNumber.getLpMargin());
                    rlp.setMarginStart(phoneNumber.getRlpMargin());
                    childViewHolder.frameLayout.setLayoutParams(lp);
                    childViewHolder.relativeLayout.setLayoutParams(rlp);
                    subItem.addView(childView);
                }
            }
            mConstraintSet.clone(detail);
            mConstraintSet.connect(R.id.contact_name,
                    ConstraintSet.END,
                    viewId,
                    ConstraintSet.START);
            mConstraintSet.applyTo(detail);
            contactNameView.setText(name);
            imageText.setText(contact.getBriefName());
            phoneNumberView.setText(number);
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
                mHandler.postDelayed(() -> {
                    mSmoothScroller.setTargetPosition(getAdapterPosition());
                    mRecyclerView.getLayoutManager().startSmoothScroll(mSmoothScroller);
                }, 100);
            }
        }
    }
}
