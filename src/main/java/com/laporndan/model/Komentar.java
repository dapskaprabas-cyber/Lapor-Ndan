package com.laporndan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "komentar")
public class Komentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_laporan", nullable = false)
    private Laporan laporan;

    @Column(name = "nama_pengguna")
    private String namaPengguna;

    @Column(name = "isi_komentar")
    private String isiKomentar;

    @Column(name = "foto_bukti")
    private String fotoBukti;

    @Column(name = "tanggal_komentar", insertable = false, updatable = false)
    private String tanggalKomentar;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Laporan getLaporan() { return laporan; }
    public void setLaporan(Laporan laporan) { this.laporan = laporan; }
    public String getNamaPengguna() { return namaPengguna; }
    public void setNamaPengguna(String namaPengguna) { this.namaPengguna = namaPengguna; }
    public String getIsiKomentar() { return isiKomentar; }
    public void setIsiKomentar(String isiKomentar) { this.isiKomentar = isiKomentar; }
    public String getFotoBukti() { return fotoBukti; }
    public void setFotoBukti(String fotoBukti) { this.fotoBukti = fotoBukti; }
    public String getTanggalKomentar() { return tanggalKomentar; }
    public void setTanggalKomentar(String tanggalKomentar) { this.tanggalKomentar = tanggalKomentar; }
}