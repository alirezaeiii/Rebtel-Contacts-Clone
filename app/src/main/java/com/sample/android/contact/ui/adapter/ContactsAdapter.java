package com.sample.android.contact.ui.adapter;

import static com.sample.android.contact.domain.ContactItemKt.isSeparator;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.ui.viewholder.BaseContactMultipleViewHolder;
import com.sample.android.contact.ui.viewholder.BaseViewHolder;
import com.sample.android.contact.ui.viewholder.ContactMultipleViewHolder;
import com.sample.android.contact.ui.viewholder.ContactViewHolder;
import com.sample.android.contact.ui.viewholder.SearchContactMultipleViewHolder;
import com.sample.android.contact.ui.viewholder.SearchContactViewHolder;
import com.sample.android.contact.ui.viewholder.SeparatorViewHolder;
import com.sample.android.contact.widget.HeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<BaseViewHolder>
        implements BaseContactMultipleViewHolder.ClickListener,
        HeaderItemDecoration.StickyHeaderInterface {

    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_CONTACT = 2;
    private static final int TYPE_CONTACT_MULTIPLE = 3;
    private static final int TYPE_SEARCH_CONTACT = 4;
    private static final int TYPE_SEARCH_CONTACT_MULTIPLE = 5;

    private final FragmentManager fragmentManager;
    private final OnItemClickListener clickListener;
    private List<ContactItem> mContacts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.SmoothScroller mSmoothScroller;
    private boolean mShowSeparator;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public ContactsAdapter(FragmentManager fragmentManager, OnItemClickListener clickListener) {
        this.fragmentManager = fragmentManager;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return switch (viewType) {
            case TYPE_CONTACT -> new ContactViewHolder(layoutInflater
                    .inflate(R.layout.contact_item, parent, false), fragmentManager, clickListener);
            case TYPE_SEARCH_CONTACT -> new SearchContactViewHolder(layoutInflater
                    .inflate(R.layout.contact_item, parent, false), fragmentManager, clickListener);
            case TYPE_SEPARATOR -> new SeparatorViewHolder(layoutInflater
                    .inflate(R.layout.contact_separator, parent, false));
            case TYPE_CONTACT_MULTIPLE -> new ContactMultipleViewHolder(layoutInflater
                    .inflate(R.layout.contact_multiple_items, parent, false), fragmentManager, this, clickListener);
            case TYPE_SEARCH_CONTACT_MULTIPLE -> new SearchContactMultipleViewHolder(layoutInflater
                    .inflate(R.layout.contact_multiple_items, parent, false), fragmentManager, this, clickListener);
            default -> throw new RuntimeException("You must supply a valid type for this adapter");
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        final ContactItem contactItem = mContacts.get(position);
        holder.bind(contactItem);
    }

    @Override
    public int getItemViewType(int position) {
        ContactItem contactItem = mContacts.get(position);
        if (!isSeparator(contactItem)) {
            if (contactItem.getContact().getType() == Contact.Type.SINGLE) {
                if (mShowSeparator) {
                    return TYPE_CONTACT;
                }
                return TYPE_SEARCH_CONTACT;
            }
            if (mShowSeparator) {
                return TYPE_CONTACT_MULTIPLE;
            }
            return TYPE_SEARCH_CONTACT_MULTIPLE;
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

    @Override
    public void onClick(int position) {

        final Contact contact = mContacts.get(position).getContact();

        boolean expanded = contact.isExpanded();
        contact.setExpanded(!expanded);
        notifyItemChanged(position);
        if (!expanded) {
            mHandler.postDelayed(() -> {
                mSmoothScroller.setTargetPosition(position);
                mRecyclerView.getLayoutManager().startSmoothScroll(mSmoothScroller);
            }, 100);
        }
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.contact_separator;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        TextView separatorText = header.findViewById(R.id.separator_text);
        View separatorView = header.findViewById(R.id.separator_view);
        if (mShowSeparator) {
            ContactItem contact = mContacts.get(headerPosition);
            separatorText.setText(contact.getContactSeparator());
        } else {
            separatorView.setVisibility(View.GONE);
            separatorText.setVisibility(View.GONE);
            header.findViewById(R.id.separator_top_line).setVisibility(View.GONE);
            header.findViewById(R.id.separator_bottom_line).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isHeader(int itemPosition) {
        ContactItem contactItem = mContacts.get(itemPosition);
        return isSeparator(contactItem);
    }

    public void setItems(List<ContactItem> contacts, boolean showSeparator) {
        mContacts = contacts;
        mShowSeparator = showSeparator;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
