package com.sample.android.contact.util;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ContactUtils {

    public static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL
    };

    private ContactUtils() {
    }

    public static List<ContactItem> getContacts(Cursor cursor, Context context) {
        List<ContactItem> contacts = new ArrayList<>();

        int nameIndex = cursor.getColumnIndex(PROJECTION[0]);
        int numberIndex = cursor.getColumnIndex(PROJECTION[1]);
        int typeIndex = cursor.getColumnIndex(PROJECTION[2]);
        int typeLabelIndex = cursor.getColumnIndex(PROJECTION[3]);

        Contact prevContact = null;
        char previousFirstChar = 0;
        while (cursor.moveToNext()) {

            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            int type = cursor.getInt(typeIndex);

            String numberType = type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM ?
                    cursor.getString(typeLabelIndex) : getTypeValue(context, type);

            number = (!number.matches("000+([0-9]+)") &&
                    number.startsWith("00")) ? "+" + number.substring(2) : number;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

            ContactPhoneNumber phoneNumber;
            String regionCode = null;
            try {
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, "");
                regionCode = phoneUtil.getRegionCodeForNumber(numberProto);
                phoneNumber = new ContactPhoneNumber(phoneUtil.format(numberProto, INTERNATIONAL), numberType);
            } catch (NumberParseException e) {
                phoneNumber = new ContactPhoneNumber(number, numberType);
            }

            Contact contact = new Contact(name);
            if (!contact.equals(prevContact)) {
                String nameAccent = getNameAccent(name);
                char nameFirstChar = nameAccent.toUpperCase().charAt(0);
                if (prevContact == null) {
                    ContactItem contactItem = new ContactItem();
                    contactItem.setContactSeparator(getContactSeparator(name));
                    contacts.add(contactItem);
                } else {
                    if (Character.isLetter(nameFirstChar) && nameFirstChar != previousFirstChar) {
                        ContactItem contactItem = new ContactItem();
                        contactItem.setContactSeparator(getContactSeparator(name));
                        contacts.add(contactItem);
                    }
                    if ((Character.isLetter(previousFirstChar) && previousFirstChar != nameFirstChar) ||
                            (!Character.isLetter(previousFirstChar) && Character.isLetter(nameFirstChar)
                                    && previousFirstChar != nameFirstChar)) {
                        prevContact.setShowBottomLine(false);
                    }
                    if ((Character.isLetter(previousFirstChar) || Character.isLetter(nameFirstChar))
                            && previousFirstChar != nameFirstChar) {
                        prevContact.setShowChildBottomLine(false);
                    }
                }

                Set<Integer> flagResIds = new LinkedHashSet<>();
                flagResIds.add(getFlagResID(context, regionCode));
                Set<ContactPhoneNumber> numbers = new LinkedHashSet<>();
                numbers.add(phoneNumber);
                contact = new Contact(name, numbers, getBriefName(name), flagResIds);
                ContactItem contactItem = new ContactItem();
                contactItem.setContact(contact);

                prevContact = contact;
                previousFirstChar = nameFirstChar;
                contacts.add(contactItem);
            } else {
                Set<ContactPhoneNumber> numbers = prevContact.getPhoneNumbers();
                Set<Integer> flagResIds = prevContact.getFlagResIds();
                if (numbers.size() == 1) {
                    if (!numbers.contains(phoneNumber)) {
                        prevContact.setType(Contact.Type.MULTIPLE);
                    }
                    Iterator<ContactPhoneNumber> numberIterator = numbers.iterator();
                    ContactPhoneNumber firstPhoneNumber = numberIterator.next();
                    firstPhoneNumber.setStartPadding((int) context.getResources().getDimension(R.dimen.dimen_frame_margin_default));
                    firstPhoneNumber.setStartMargin((int) context.getResources().getDimension(R.dimen.dimen_relative_margin_default));
                    firstPhoneNumber.setFlagResId(flagResIds.iterator().next());
                }
                phoneNumber.setStartPadding((int) context.getResources().getDimension(R.dimen.dimen_frame_margin));
                phoneNumber.setStartMargin(0);
                phoneNumber.setFlagResId(getFlagResID(context, regionCode));
                numbers.add(phoneNumber);
                flagResIds.add(phoneNumber.getFlagResId());
            }
        }
        if (prevContact != null) {
            prevContact.setShowBottomLine(false);
            prevContact.setShowChildBottomLine(false);
        }
        return contacts;
    }

    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /* Helper Methods */

    private static String getContactSeparator(String name) {
        char ch = name.toUpperCase().charAt(0);
        return String.valueOf(Character.isLetter(ch) ? ch : '&');
    }

    private static String getNameAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    private static String getTypeValue(Context context, int type) {
        return context.getString(switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> R.string.home;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> R.string.mobile;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> R.string.work;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> R.string.work_fax;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> R.string.home_fax;
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> R.string.pager;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> R.string.main;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> R.string.other;
            default -> R.string.empty;
        });
    }

    private static String getBriefName(String name) {
        String[] splitName = name.split("\\s+");
        char character;
        int index;
        String brief = "¿";

        for (index = 0; index < splitName.length; index++) {
            character = splitName[index].toUpperCase().charAt(0);
            if (Character.isLetter(character)) {
                brief = String.valueOf(character);
                break;
            }
        }

        for (int i = index + 1; i < splitName.length; i++) {
            character = splitName[i].toUpperCase().charAt(0);
            if (Character.isLetter(character)) {
                brief += "." + character;
                break;
            }
        }
        return brief;
    }

    private static int getFlagResID(Context context, String regionCode) {
        if (regionCode == null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? getFlagResID(tm.getSimCountryIso()) : R.drawable.flag_transparent;
        }
        return getFlagResID(regionCode);
    }

    private static int getFlagResID(String regionCode) {
        return switch (regionCode.toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad" -> //andorra
                    R.drawable.flag_andorra;
            case "ae" -> //united arab emirates
                    R.drawable.flag_uae;
            case "af" -> //afghanistan
                    R.drawable.flag_afghanistan;
            case "ag" -> //antigua & barbuda
                    R.drawable.flag_antigua_and_barbuda;
            case "ai" -> //anguilla // Caribbean Islands
                    R.drawable.flag_anguilla;
            case "al" -> //albania
                    R.drawable.flag_albania;
            case "am" -> //armenia
                    R.drawable.flag_armenia;
            case "ao" -> //angola
                    R.drawable.flag_angola;
            case "aq" -> //antarctica // custom
                    R.drawable.flag_antarctica;
            case "ar" -> //argentina
                    R.drawable.flag_argentina;
            case "as" -> //American Samoa
                    R.drawable.flag_american_samoa;
            case "at" -> //austria
                    R.drawable.flag_austria;
            case "au" -> //australia
                    R.drawable.flag_australia;
            case "aw" -> //aruba
                    R.drawable.flag_aruba;
            case "ax" -> //alan islands
                    R.drawable.flag_aland;
            case "az" -> //azerbaijan
                    R.drawable.flag_azerbaijan;
            case "ba" -> //bosnia and herzegovina
                    R.drawable.flag_bosnia;
            case "bb" -> //barbados
                    R.drawable.flag_barbados;
            case "bd" -> //bangladesh
                    R.drawable.flag_bangladesh;
            case "be" -> //belgium
                    R.drawable.flag_belgium;
            case "bf" -> //burkina faso
                    R.drawable.flag_burkina_faso;
            case "bg" -> //bulgaria
                    R.drawable.flag_bulgaria;
            case "bh" -> //bahrain
                    R.drawable.flag_bahrain;
            case "bi" -> //burundi
                    R.drawable.flag_burundi;
            case "bj" -> //benin
                    R.drawable.flag_benin;
            case "bl" -> //saint barthélemy
                    R.drawable.flag_saint_barthelemy;// custom
            case "bm" -> //bermuda
                    R.drawable.flag_bermuda;
            case "bn" -> //brunei darussalam // custom
                    R.drawable.flag_brunei;
            case "bo" -> //bolivia, plurinational state of
                    R.drawable.flag_bolivia;
            case "br" -> //brazil
                    R.drawable.flag_brazil;
            case "bs" -> //bahamas
                    R.drawable.flag_bahamas;
            case "bt" -> //bhutan
                    R.drawable.flag_bhutan;
            case "bw" -> //botswana
                    R.drawable.flag_botswana;
            case "by" -> //belarus
                    R.drawable.flag_belarus;
            case "bz" -> //belize
                    R.drawable.flag_belize;
            case "ca" -> //canada
                    R.drawable.flag_canada;
            case "cc" -> //cocos (keeling) islands
                    R.drawable.flag_cocos;// custom
            case "cd" -> //congo, the democratic republic of the
                    R.drawable.flag_democratic_republic_of_the_congo;
            case "cf" -> //central african republic
                    R.drawable.flag_central_african_republic;
            case "cg" -> //congo
                    R.drawable.flag_republic_of_the_congo;
            case "ch" -> //switzerland
                    R.drawable.flag_switzerland;
            case "ci" -> //côte d\'ivoire
                    R.drawable.flag_cote_divoire;
            case "ck" -> //cook islands
                    R.drawable.flag_cook_islands;
            case "cl" -> //chile
                    R.drawable.flag_chile;
            case "cm" -> //cameroon
                    R.drawable.flag_cameroon;
            case "cn" -> //china
                    R.drawable.flag_china;
            case "co" -> //colombia
                    R.drawable.flag_colombia;
            case "cr" -> //costa rica
                    R.drawable.flag_costa_rica;
            case "cu" -> //cuba
                    R.drawable.flag_cuba;
            case "cv" -> //cape verde
                    R.drawable.flag_cape_verde;
            case "cx" -> //christmas island
                    R.drawable.flag_christmas_island;
            case "cy" -> //cyprus
                    R.drawable.flag_cyprus;
            case "cz" -> //czech republic
                    R.drawable.flag_czech_republic;
            case "de" -> //germany
                    R.drawable.flag_germany;
            case "dj" -> //djibouti
                    R.drawable.flag_djibouti;
            case "dk" -> //denmark
                    R.drawable.flag_denmark;
            case "dm" -> //dominica
                    R.drawable.flag_dominica;
            case "do" -> //dominican republic
                    R.drawable.flag_dominican_republic;
            case "dz" -> //algeria
                    R.drawable.flag_algeria;
            case "ec" -> //ecuador
                    R.drawable.flag_ecuador;
            case "ee" -> //estonia
                    R.drawable.flag_estonia;
            case "eg" -> //egypt
                    R.drawable.flag_egypt;
            case "er" -> //eritrea
                    R.drawable.flag_eritrea;
            case "es" -> //spain
                    R.drawable.flag_spain;
            case "et" -> //ethiopia
                    R.drawable.flag_ethiopia;
            case "fi" -> //finland
                    R.drawable.flag_finland;
            case "fj" -> //fiji
                    R.drawable.flag_fiji;
            case "fk" -> //falkland islands (malvinas)
                    R.drawable.flag_falkland_islands;
            case "fm" -> //micronesia, federated states of
                    R.drawable.flag_micronesia;
            case "fo" -> //faroe islands
                    R.drawable.flag_faroe_islands;
            case "fr" -> //france
                    R.drawable.flag_france;
            case "ga" -> //gabon
                    R.drawable.flag_gabon;
            case "gb" -> //united kingdom
                    R.drawable.flag_united_kingdom;
            case "gd" -> //grenada
                    R.drawable.flag_grenada;
            case "ge" -> //georgia
                    R.drawable.flag_georgia;
            case "gf" -> //guyane
                    R.drawable.flag_guyane;
            case "gg" -> //Guernsey
                    R.drawable.flag_guernsey;
            case "gh" -> //ghana
                    R.drawable.flag_ghana;
            case "gi" -> //gibraltar
                    R.drawable.flag_gibraltar;
            case "gl" -> //greenland
                    R.drawable.flag_greenland;
            case "gm" -> //gambia
                    R.drawable.flag_gambia;
            case "gn" -> //guinea
                    R.drawable.flag_guinea;
            case "gp" -> //guadeloupe
                    R.drawable.flag_guadeloupe;
            case "gq" -> //equatorial guinea
                    R.drawable.flag_equatorial_guinea;
            case "gr" -> //greece
                    R.drawable.flag_greece;
            case "gt" -> //guatemala
                    R.drawable.flag_guatemala;
            case "gu" -> //Guam
                    R.drawable.flag_guam;
            case "gw" -> //guinea-bissau
                    R.drawable.flag_guinea_bissau;
            case "gy" -> //guyana
                    R.drawable.flag_guyana;
            case "hk" -> //hong kong
                    R.drawable.flag_hong_kong;
            case "hn" -> //honduras
                    R.drawable.flag_honduras;
            case "hr" -> //croatia
                    R.drawable.flag_croatia;
            case "ht" -> //haiti
                    R.drawable.flag_haiti;
            case "hu" -> //hungary
                    R.drawable.flag_hungary;
            case "id" -> //indonesia
                    R.drawable.flag_indonesia;
            case "ie" -> //ireland
                    R.drawable.flag_ireland;
            case "il" -> //israel
                    R.drawable.flag_israel;
            case "im" -> //isle of man
                    R.drawable.flag_isleof_man; // custom
            case "is" -> //Iceland
                    R.drawable.flag_iceland;
            case "in" -> //india
                    R.drawable.flag_india;
            case "io" -> //British indian ocean territory
                    R.drawable.flag_british_indian_ocean_territory;
            case "iq" -> //iraq
                    R.drawable.flag_iraq_new;
            case "ir" -> //iran, islamic republic of
                    R.drawable.flag_iran;
            case "it" -> //italy
                    R.drawable.flag_italy;
            case "je" -> //Jersey
                    R.drawable.flag_jersey;
            case "jm" -> //jamaica
                    R.drawable.flag_jamaica;
            case "jo" -> //jordan
                    R.drawable.flag_jordan;
            case "jp" -> //japan
                    R.drawable.flag_japan;
            case "ke" -> //kenya
                    R.drawable.flag_kenya;
            case "kg" -> //kyrgyzstan
                    R.drawable.flag_kyrgyzstan;
            case "kh" -> //cambodia
                    R.drawable.flag_cambodia;
            case "ki" -> //kiribati
                    R.drawable.flag_kiribati;
            case "km" -> //comoros
                    R.drawable.flag_comoros;
            case "kn" -> //st kitts & nevis
                    R.drawable.flag_saint_kitts_and_nevis;
            case "kp" -> //north korea
                    R.drawable.flag_north_korea;
            case "kr" -> //south korea
                    R.drawable.flag_south_korea;
            case "kw" -> //kuwait
                    R.drawable.flag_kuwait;
            case "ky" -> //Cayman_Islands
                    R.drawable.flag_cayman_islands;
            case "kz" -> //kazakhstan
                    R.drawable.flag_kazakhstan;
            case "la" -> //lao people\'s democratic republic
                    R.drawable.flag_laos;
            case "lb" -> //lebanon
                    R.drawable.flag_lebanon;
            case "lc" -> //st lucia
                    R.drawable.flag_saint_lucia;
            case "li" -> //liechtenstein
                    R.drawable.flag_liechtenstein;
            case "lk" -> //sri lanka
                    R.drawable.flag_sri_lanka;
            case "lr" -> //liberia
                    R.drawable.flag_liberia;
            case "ls" -> //lesotho
                    R.drawable.flag_lesotho;
            case "lt" -> //lithuania
                    R.drawable.flag_lithuania;
            case "lu" -> //luxembourg
                    R.drawable.flag_luxembourg;
            case "lv" -> //latvia
                    R.drawable.flag_latvia;
            case "ly" -> //libya
                    R.drawable.flag_libya;
            case "ma" -> //morocco
                    R.drawable.flag_morocco;
            case "mc" -> //monaco
                    R.drawable.flag_monaco;
            case "md" -> //moldova, republic of
                    R.drawable.flag_moldova;
            case "me" -> //montenegro
                    R.drawable.flag_of_montenegro;// custom
            case "mf" -> R.drawable.flag_saint_martin;
            case "mg" -> //madagascar
                    R.drawable.flag_madagascar;
            case "mh" -> //marshall islands
                    R.drawable.flag_marshall_islands;
            case "mk" -> //macedonia, the former yugoslav republic of
                    R.drawable.flag_macedonia;
            case "ml" -> //mali
                    R.drawable.flag_mali;
            case "mm" -> //myanmar
                    R.drawable.flag_myanmar;
            case "mn" -> //mongolia
                    R.drawable.flag_mongolia;
            case "mo" -> //macao
                    R.drawable.flag_macao;
            case "mp" -> // Northern mariana islands
                    R.drawable.flag_northern_mariana_islands;
            case "mq" -> //martinique
                    R.drawable.flag_martinique;
            case "mr" -> //mauritania
                    R.drawable.flag_mauritania;
            case "ms" -> //montserrat
                    R.drawable.flag_montserrat;
            case "mt" -> //malta
                    R.drawable.flag_malta;
            case "mu" -> //mauritius
                    R.drawable.flag_mauritius;
            case "mv" -> //maldives
                    R.drawable.flag_maldives;
            case "mw" -> //malawi
                    R.drawable.flag_malawi;
            case "mx" -> //mexico
                    R.drawable.flag_mexico;
            case "my" -> //malaysia
                    R.drawable.flag_malaysia;
            case "mz" -> //mozambique
                    R.drawable.flag_mozambique;
            case "na" -> //namibia
                    R.drawable.flag_namibia;
            case "nc" -> //new caledonia
                    R.drawable.flag_new_caledonia;// custom
            case "ne" -> //niger
                    R.drawable.flag_niger;
            case "nf" -> //Norfolk
                    R.drawable.flag_norfolk_island;
            case "ng" -> //nigeria
                    R.drawable.flag_nigeria;
            case "ni" -> //nicaragua
                    R.drawable.flag_nicaragua;
            case "nl" -> //netherlands
                    R.drawable.flag_netherlands;
            case "no" -> //norway
                    R.drawable.flag_norway;
            case "np" -> //nepal
                    R.drawable.flag_nepal;
            case "nr" -> //nauru
                    R.drawable.flag_nauru;
            case "nu" -> //niue
                    R.drawable.flag_niue;
            case "nz" -> //new zealand
                    R.drawable.flag_new_zealand;
            case "om" -> //oman
                    R.drawable.flag_oman;
            case "pa" -> //panama
                    R.drawable.flag_panama;
            case "pe" -> //peru
                    R.drawable.flag_peru;
            case "pf" -> //french polynesia
                    R.drawable.flag_french_polynesia;
            case "pg" -> //papua new guinea
                    R.drawable.flag_papua_new_guinea;
            case "ph" -> //philippines
                    R.drawable.flag_philippines;
            case "pk" -> //pakistan
                    R.drawable.flag_pakistan;
            case "pl" -> //poland
                    R.drawable.flag_poland;
            case "pm" -> //saint pierre and miquelon
                    R.drawable.flag_saint_pierre;
            case "pn" -> //pitcairn
                    R.drawable.flag_pitcairn_islands;
            case "pr" -> //puerto rico
                    R.drawable.flag_puerto_rico;
            case "ps" -> //palestine
                    R.drawable.flag_palestine;
            case "pt" -> //portugal
                    R.drawable.flag_portugal;
            case "pw" -> //palau
                    R.drawable.flag_palau;
            case "py" -> //paraguay
                    R.drawable.flag_paraguay;
            case "qa" -> //qatar
                    R.drawable.flag_qatar;
            case "re" -> //la reunion
                    R.drawable.flag_martinique; // no exact flag found
            case "ro" -> //romania
                    R.drawable.flag_romania;
            case "rs" -> //serbia
                    R.drawable.flag_serbia; // custom
            case "ru" -> //russian federation
                    R.drawable.flag_russian_federation;
            case "rw" -> //rwanda
                    R.drawable.flag_rwanda;
            case "sa" -> //saudi arabia
                    R.drawable.flag_saudi_arabia;
            case "sb" -> //solomon islands
                    R.drawable.flag_soloman_islands;
            case "sc" -> //seychelles
                    R.drawable.flag_seychelles;
            case "sd" -> //sudan
                    R.drawable.flag_sudan;
            case "se" -> //sweden
                    R.drawable.flag_sweden;
            case "sg" -> //singapore
                    R.drawable.flag_singapore;
            case "sh" -> //saint helena, ascension and tristan da cunha
                    R.drawable.flag_saint_helena; // custom
            case "si" -> //slovenia
                    R.drawable.flag_slovenia;
            case "sk" -> //slovakia
                    R.drawable.flag_slovakia;
            case "sl" -> //sierra leone
                    R.drawable.flag_sierra_leone;
            case "sm" -> //san marino
                    R.drawable.flag_san_marino;
            case "sn" -> //senegal
                    R.drawable.flag_senegal;
            case "so" -> //somalia
                    R.drawable.flag_somalia;
            case "sr" -> //suriname
                    R.drawable.flag_suriname;
            case "ss" -> //south sudan
                    R.drawable.flag_south_sudan;
            case "st" -> //sao tome and principe
                    R.drawable.flag_sao_tome_and_principe;
            case "sv" -> //el salvador
                    R.drawable.flag_el_salvador;
            case "sx" -> //sint maarten
                    R.drawable.flag_sint_maarten;
            case "sy" -> //syrian arab republic
                    R.drawable.flag_syria;
            case "sz" -> //swaziland
                    R.drawable.flag_swaziland;
            case "tc" -> //turks & caicos islands
                    R.drawable.flag_turks_and_caicos_islands;
            case "td" -> //chad
                    R.drawable.flag_chad;
            case "tg" -> //togo
                    R.drawable.flag_togo;
            case "th" -> //thailand
                    R.drawable.flag_thailand;
            case "tj" -> //tajikistan
                    R.drawable.flag_tajikistan;
            case "tk" -> //tokelau
                    R.drawable.flag_tokelau; // custom
            case "tl" -> //timor-leste
                    R.drawable.flag_timor_leste;
            case "tm" -> //turkmenistan
                    R.drawable.flag_turkmenistan;
            case "tn" -> //tunisia
                    R.drawable.flag_tunisia;
            case "to" -> //tonga
                    R.drawable.flag_tonga;
            case "tr" -> //turkey
                    R.drawable.flag_turkey;
            case "tt" -> //trinidad & tobago
                    R.drawable.flag_trinidad_and_tobago;
            case "tv" -> //tuvalu
                    R.drawable.flag_tuvalu;
            case "tw" -> //taiwan, province of china
                    R.drawable.flag_taiwan;
            case "tz" -> //tanzania, united republic of
                    R.drawable.flag_tanzania;
            case "ua" -> //ukraine
                    R.drawable.flag_ukraine;
            case "ug" -> //uganda
                    R.drawable.flag_uganda;
            case "us" -> //united states
                    R.drawable.flag_united_states_of_america;
            case "uy" -> //uruguay
                    R.drawable.flag_uruguay;
            case "uz" -> //uzbekistan
                    R.drawable.flag_uzbekistan;
            case "va" -> //holy see (vatican city state)
                    R.drawable.flag_vatican_city;
            case "vc" -> //st vincent & the grenadines
                    R.drawable.flag_saint_vicent_and_the_grenadines;
            case "ve" -> //venezuela, bolivarian republic of
                    R.drawable.flag_venezuela;
            case "vg" -> //british virgin islands
                    R.drawable.flag_british_virgin_islands;
            case "vi" -> //us virgin islands
                    R.drawable.flag_us_virgin_islands;
            case "vn" -> //vietnam
                    R.drawable.flag_vietnam;
            case "vu" -> //vanuatu
                    R.drawable.flag_vanuatu;
            case "wf" -> //wallis and futuna
                    R.drawable.flag_wallis_and_futuna;
            case "ws" -> //samoa
                    R.drawable.flag_samoa;
            case "xk" -> //kosovo
                    R.drawable.flag_kosovo;
            case "ye" -> //yemen
                    R.drawable.flag_yemen;
            case "yt" -> //mayotte
                    R.drawable.flag_martinique; // no exact flag found
            case "za" -> //south africa
                    R.drawable.flag_south_africa;
            case "zm" -> //zambia
                    R.drawable.flag_zambia;
            case "zw" -> //zimbabwe
                    R.drawable.flag_zimbabwe;
            default -> R.drawable.flag_transparent;
        };
    }
}
