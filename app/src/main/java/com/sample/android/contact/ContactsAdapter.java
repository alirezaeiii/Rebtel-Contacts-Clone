package com.sample.android.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static com.sample.android.contact.Utils.deAccent;
import static com.sample.android.contact.Utils.getTypeValue;

public class ContactsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Contact> mContacts;

    // State of the row that needs to show separator
    private static final int SECTIONED_STATE = 1;
    // State of the row that need not show separator
    private static final int REGULAR_STATE = 2;
    // Cache row states based on positions
    private int[] mSeparatorRowStates;
    private int[] mLineRowStates;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        mContext = context;
        mContacts = contacts;
        mSeparatorRowStates = new int[getGroupCount()];
        mLineRowStates = new int[getGroupCount()];
    }

    @Override
    public int getGroupCount() {
        return mContacts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return children count
        int count = mContacts.get(groupPosition).getPhoneNumbers().size();
        return count == 1 ? 0 : count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Get header position
        return mContacts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // This will return the child
        return mContacts.get(groupPosition).getPhoneNumbers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        boolean showSeparator = false;
        boolean showLine = true;

        Contact contact = (Contact) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_item, null);
        } else {
            view = convertView;
        }

        // Set contact name and number
        TextView contactNameView = (TextView) view.findViewById(R.id.contact_name);
        TextView phoneNumberView = (TextView) view.findViewById(R.id.phone_number);
        TextView phoneNumberType = (TextView) view.findViewById(R.id.phone_type);
        TextView lineNumber = (TextView) view.findViewById(R.id.line_number);

        String name = contact.getName();
        List<PhoneNumber> numbers = contact.getPhoneNumbers();
        String number = numbers.size() == 1 ? numbers.get(0).getNumber() : "";

        contactNameView.setText(name);
        phoneNumberView.setText(number);
        if (numbers.size() == 1) {
            phoneNumberType.setVisibility(View.VISIBLE);
            lineNumber.setVisibility(View.INVISIBLE);
            phoneNumberType.setText(getTypeValue(numbers.get(0).getType()));
        } else {
            lineNumber.setVisibility(View.VISIBLE);
            phoneNumberType.setVisibility(View.INVISIBLE);
            lineNumber.setText(String.valueOf(numbers.size()));
        }

        TextView imageText = view.findViewById(R.id.image_text);
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

        // Show separator ?
        switch (mSeparatorRowStates[groupPosition]) {

            case SECTIONED_STATE:
                showSeparator = true;
                break;

            case REGULAR_STATE:
                showSeparator = false;
                break;

            default:

                if (groupPosition == 0) {
                    showSeparator = true;
                } else {
                    Contact previousContact = (Contact) getGroup(groupPosition - 1);

                    String previousName = deAccent(previousContact.getName());
                    char[] previousNameArray = previousName.toUpperCase().toCharArray();
                    char[] nameArray = deAccent(name).toUpperCase().toCharArray();

                    if (Character.isLetter(nameArray[0]) &&
                            nameArray[0] != previousNameArray[0]) {
                        showSeparator = true;
                    }
                }

                // Cache it
                mSeparatorRowStates[groupPosition] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;

                break;
        }

        View separatorView = (View) view.findViewById(R.id.separator);

        if (showSeparator) {
            TextView separatorText = (TextView) view.findViewById(R.id.separator_text);
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


        // Show separator ?
        switch (mLineRowStates[groupPosition]) {

            case SECTIONED_STATE:
                showLine = false;
                break;

            case REGULAR_STATE:
                showLine = true;
                break;

            default:

                if (groupPosition == mContacts.size() - 1) {
                    showLine = false;
                } else {
                    Contact nextContact = (Contact) getGroup(groupPosition + 1);

                    String nextName = deAccent(nextContact.getName());
                    char[] nextNameArray = nextName.toUpperCase().toCharArray();
                    char[] nameArray = deAccent(name).toUpperCase().toCharArray();

                    if ((Character.isLetter(nameArray[0]) && nameArray[0] != nextNameArray[0]) ||
                            (!Character.isLetter(nameArray[0]) && Character.isLetter(nextNameArray[0])
                                    && nameArray[0] != nextNameArray[0])) {
                        showLine = false;
                    }
                }

                // Cache it
                mLineRowStates[groupPosition] = showLine ? REGULAR_STATE : SECTIONED_STATE;

                break;
        }

        View line = view.findViewById(R.id.line);
        line.setVisibility(showLine ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // Getting child text
        final PhoneNumber phoneNumber = (PhoneNumber) getChild(groupPosition, childPosition);
        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, parent, false);
        }

        //set content in the child views
        TextView contactNumber = (TextView) convertView.findViewById(R.id.contact_number);
        TextView numberType = (TextView) convertView.findViewById(R.id.type);

        contactNumber.setText(phoneNumber.getNumber());
        numberType.setText(getTypeValue(phoneNumber.getType()));

        boolean lineFlag = true;

        if (groupPosition == mContacts.size() - 1) {
            lineFlag = false;
        } else {
            Contact nextContact = (Contact) getGroup(groupPosition + 1);

            String nextName = deAccent(nextContact.getName());
            char[] nextNameArray = nextName.toUpperCase().toCharArray();
            char[] nameArray = deAccent(mContacts.get(groupPosition).getName()).toUpperCase().toCharArray();

            if ((Character.isLetter(nameArray[0]) || Character.isLetter(nextNameArray[0]))
                    && nameArray[0] != nextNameArray[0]) {
                lineFlag = false;
            }
        }

        View childLine = convertView.findViewById(R.id.child_line);
        childLine.setVisibility(lineFlag ? View.VISIBLE : View.GONE);

        View childTopLine = convertView.findViewById(R.id.child_top_line);
        childTopLine.setVisibility(lineFlag ? View.GONE : View.VISIBLE);

        View frameLayout = convertView.findViewById(R.id.frameLayout);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        View relativeLayout = convertView.findViewById(R.id.relativeLayout);
        FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (lineFlag) {
            lp.setMarginStart((int) mContext.getResources().getDimension(R.dimen.dimen_frame_margin_default));
            frameLayout.setLayoutParams(lp);

            rlp.setMarginStart((int) mContext.getResources().getDimension(R.dimen.dimen_relative_margin_default));
            relativeLayout.setLayoutParams(rlp);
        } else {

            if (childPosition == 0) {
                lp.setMarginStart((int) mContext.getResources().getDimension(R.dimen.dimen_frame_margin_default));
                frameLayout.setLayoutParams(lp);

                rlp.setMarginStart((int) mContext.getResources().getDimension(R.dimen.dimen_relative_margin_default));
                relativeLayout.setLayoutParams(rlp);

            } else {

                lp.setMarginStart((int) mContext.getResources().getDimension(R.dimen.dimen_frame_margin));
                frameLayout.setLayoutParams(lp);

                rlp.setMarginStart(0);
                relativeLayout.setLayoutParams(rlp);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
