package com.laporndan.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "laporan")
public class Laporan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama_pelapor")
    private String namaPelapor;

    private String tanggal;
    private String waktu;
    private String lokasi;
    private int kedalaman;
    private String foto;
    private String status;

    @OneToMany(mappedBy = "laporan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Komentar> daftarKomentar;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaPelapor() { return namaPelapor; }
    public void setNamaPelapor(String namaPelapor) { this.namaPelapor = namaPelapor; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getWaktu() { return waktu; }
    public void setWaktu(String waktu) { this.waktu = waktu; }
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public int getKedalaman() { return kedalaman; }
    public void setKedalaman(int kedalaman) { this.kedalaman = kedalaman; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Komentar> getDaftarKomentar() { return daftarKomentar; }
    public void setDaftarKomentar(List<Komentar> daftarKomentar) { this.daftarKomentar = daftarKomentar; }
}