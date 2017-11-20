package pisangmolen.com.ajari.Model;

/**
 * Created by user on 11/13/2017.
 */

public class BookingProgress {
    private String type;
    private String materi;
    private String status;
    private boolean isConfirmed;
    private String tanggalMulai;

    public BookingProgress(String type, String materi, String status, String tanggalMulai){
        this.type = type;
        this.materi = materi;
        this.status = status;
        this.tanggalMulai = tanggalMulai;
        this.isConfirmed = isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
