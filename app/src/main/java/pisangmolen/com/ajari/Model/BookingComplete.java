package pisangmolen.com.ajari.Model;

/**
 * Created by user on 11/13/2017.
 */

public class BookingComplete {
    private String type;
    private String materi;
    private String mentor;
    private String tanggalMulai;

    public BookingComplete(String type, String materi, String mentor, String tanggalMulai){
        this.type = type;
        this.materi = materi;
        this.mentor =mentor;
        this.tanggalMulai = tanggalMulai;
    }

    public String getType() {
        return type;
    }

    public String getMateri() {
        return materi;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public String getMentor() {
        return mentor;
    }
}
