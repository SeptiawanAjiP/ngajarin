package pisangmolen.com.ajari.Helper;

/**
 * Created by aji on 11/19/2017.
 */

public class Connection {
    private static final String ALAMAT_SERVER = "http://192.168.43.144/api/public/";

    private static final String LOGIN = "login";
    private static final String GET_DATA_BY_PHONE = "dataByPhone";
    private static final String REGISTER_PHONE = "registerPhone";
    private static final String USER_POIN = "userPoin";
    private static final String UPLOAD_FOTO = "uploadFoto";
    private static final String FORM_FIELD = "formField";
    private static final String PRICE = "price";
    private static final String BOOK = "formBook";
    private static final String TERIMA_BOOK = "terimaBook";
    private static final String BATAL_BOOK = "batalBook";
    private static final String LIST_BOOK = "listBook";
    private static final String LIST_HISTORY_BOOK = "listHistoryBook";

    public static String getAlamatServer() {
        return ALAMAT_SERVER;
    }

    public static String getLOGIN() {
        return LOGIN;
    }

    public static String getUserPoin() {
        return USER_POIN;
    }

    public static String getUploadFoto() {
        return UPLOAD_FOTO;
    }

    public static String getFormField() {
        return FORM_FIELD;
    }

    public static String getPRICE() {
        return PRICE;
    }

    public static String getBOOK() {
        return BOOK;
    }

    public static String getTerimaBook() {
        return TERIMA_BOOK;
    }

    public static String getBatalBook() {
        return BATAL_BOOK;
    }

    public static String getListBook() {
        return LIST_BOOK;
    }

    public static String getGetDataByPhone() {
        return GET_DATA_BY_PHONE;
    }

    public static String getRegisterPhone() {
        return REGISTER_PHONE;
    }

    public static String getListHistoryBook() {
        return LIST_HISTORY_BOOK;
    }
}
