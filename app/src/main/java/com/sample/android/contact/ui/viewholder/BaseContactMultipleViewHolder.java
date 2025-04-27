package com.sample.android.contact.ui.viewholder;

import static com.sample.android.contact.util.ContactUtils.openCallDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;

import com.sample.android.contact.R;
import com.sample.android.contact.databinding.ContactChildItemBinding;
import com.sample.android.contact.databinding.ContactMultipleItemsBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.ui.adapter.ContactsAdapter;

import java.util.Set;

public abstract class BaseContactMultipleViewHolder extends BaseViewHolder {

    private final ContactMultipleItemsBinding binding;

    private final Context context;

    private final FragmentManager fragmentManager;

    private final ClickListener clickListener;

    private final ContactsAdapter.OnItemClickListener onItemClickListener;

    public BaseContactMultipleViewHolder(View root, FragmentManager fragmentManager, ClickListener clickListener,
                                         ContactsAdapter.OnItemClickListener onItemClickListener) {
        super(root);
        binding = ContactMultipleItemsBinding.bind(root);
        context = root.getContext();
        this.fragmentManager = fragmentManager;
        this.clickListener = clickListener;
        this.onItemClickListener = onItemClickListener;
    }

    protected abstract boolean isBottomLineVisible();

    protected abstract boolean isChildBottomLineVisible();

    @Override
    public void bind() {
        Contact contact = contactItem.getContact();
        binding.flagItem.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_width),
                (int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_height));
        params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dimen_flag_image_view_margin_end));
        for (int flagResId : contact.getFlagResIds()) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setImageResource(flagResId);
            binding.flagItem.addView(imageView);
        }
        binding.bottomLine.setVisibility(isBottomLineVisible() ? View.VISIBLE : View.GONE);
        binding.subItem.removeAllViews();
        Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
        binding.lineNumber.setText(String.valueOf(numbers.size()));
        binding.contactName.setText(contact.getName());
        binding.imageText.setText(contact.getBriefName());
        binding.subItem.setVisibility(contact.isExpanded() ? View.VISIBLE : View.GONE);
        for (ContactPhoneNumber phoneNumber : numbers) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View childView = inflater.inflate(R.layout.contact_child_item, null);

            ChildViewHolder childViewHolder = new ChildViewHolder(childView);
            childViewHolder.binding.contactNumber.setText(phoneNumber.getNumber());
            childViewHolder.binding.type.setText(phoneNumber.getTypeLabel());
            childViewHolder.binding.flagImageView.setImageResource(phoneNumber.getFlagResId());

            if (isChildBottomLineVisible()) {
                childViewHolder.binding.childBottomLine.setVisibility(View.VISIBLE);
                childViewHolder.binding.childTopLine.setVisibility(View.GONE);
            } else {
                childViewHolder.binding.childTopLine.setVisibility(View.VISIBLE);
                childViewHolder.binding.childBottomLine.setVisibility(View.GONE);
            }

            FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    (int) context.getResources().getDimension(R.dimen.dimen_child_contact_item_height));
            rlp.setMarginStart(phoneNumber.getStartMargin());
            childViewHolder.binding.relativeLayout.setLayoutParams(rlp);
            childViewHolder.binding.frameLayout.setPadding(phoneNumber.getStartPadding(), 0, 0, 0);
            childViewHolder.binding.frameLayout.setOnClickListener(view -> openCallDialog(contact.getName(),
                    phoneNumber.getNumber(), phoneNumber.getFlagResId(), fragmentManager,
                    onItemClickListener));
            binding.subItem.addView(childView);
        }
        binding.detail.setOnClickListener(view -> clickListener.onClick(getAdapterPosition()));
    }

    static class ChildViewHolder {

        ContactChildItemBinding binding;

        public ChildViewHolder(View view) {
            binding = ContactChildItemBinding.bind(view);
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }
}
