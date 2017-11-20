package pisangmolen.com.ajari.Utils;

/**
 * Created by user on 11/13/2017.
 */

public class Konstanta {

    public final static int TYPE_SD = 1;
    public final static int TYPE_SMP = 2;
    public final static int TYPE_SMA = 3;
    public final static int TYPE_MENGAJI = 4;
    public final static int TYPE_MUSIK = 5;
    public final static int TYPE_LUKIS = 6;

    public final static String TAG_JENJANG = "jenjang-sekolah";

    public static String jenjangToString(int type){
        switch (type){
            case TYPE_SD: return "SD";
            case TYPE_SMP: return "SMP";
            case TYPE_SMA: return "SMA";
            case TYPE_MENGAJI: return "Mengaji";
            case TYPE_MUSIK: return "Musik";
            case TYPE_LUKIS: return "Lukis";
        }
        return null;
    }

}
