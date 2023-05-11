package arko.mycontacts;

public class Contact {

    private String id;
    private String nama;
    private String telp;
    private String alamat;
    private String email;
    private String foto;

    public Contact() {
    }

    public Contact(String id, String nama, String telp, String alamat, String email, String foto) {
        this.id = id;
        this.nama = nama;
        this.telp = telp;
        this.alamat = alamat;
        this.email = email;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
