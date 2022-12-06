package com.sample.android.contact.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseContactMultipleViewHolder extends BaseViewHolder {

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

    private final Context context;

    private final ClickListener clickListener;

    public BaseContactMultipleViewHolder(View root, ClickListener clickListener) {
        super(root);
        context = root.getContext();
        this.clickListener = clickListener;
        ButterKnife.bind(this, root);
    }

    protected abstract int getBottomLineVisibility();

    protected abstract boolean getShowChildBottomLine();

    @Override
    public void bind() {
        Contact contact = contactItem.getContact();
        flagItem.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_width),
                (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_height));
        params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_margin_end));
        for (int flagResId : contact.getFlagResIds()) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setImageResource(flagResId);
            flagItem.addView(imageView);
        }
        bottomLine.setVisibility(getBottomLineVisibility());
        subItem.removeAllViews();
        Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
        lineNumber.setText(String.valueOf(numbers.size()));
        contactNameView.setText(contact.getName());
        imageText.setText(contact.getBriefName());
        subItem.setVisibility(contact.isExpanded() ? View.VISIBLE : View.GONE);
        for (ContactPhoneNumber phoneNumber : numbers) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View childView = inflater.inflate(R.layout.contact_child_item, null);

            ChildViewHolder childViewHolder = new ChildViewHolder(childView);
            childViewHolder.contactNumber.setText(phoneNumber.getNumber());
            childViewHolder.numberType.setText(phoneNumber.getTypeLabel());
            childViewHolder.flagImageView.setImageResource(phoneNumber.getFlagResId());

            if (getShowChildBottomLine()) {
                childViewHolder.childBottomLine.setVisibility(View.VISIBLE);
                childViewHolder.childTopLine.setVisibility(View.GONE);
            } else {
                childViewHolder.childTopLine.setVisibility(View.VISIBLE);
                childViewHolder.childBottomLine.setVisibility(View.GONE);
            }

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
        clickListener.onClick(getAdapterPosition());
    }

    class ChildViewHolder {

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

    public interface ClickListener {
        void onClick(int position);
    }
}
