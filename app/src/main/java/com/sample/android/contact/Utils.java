package com.sample.android.contact;

import android.provider.ContactsContract;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String getTypeValue(int type) {
        String typeValue;
        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                typeValue = "Home";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                typeValue = "Mobile";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                typeValue = "Work";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                typeValue = "Work Fax";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                typeValue = "Home Fax";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                typeValue = "Pager";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                typeValue = "Main";
                break;
            default:
                typeValue = "Other";
                break;
        }
        return typeValue;
    }
}
