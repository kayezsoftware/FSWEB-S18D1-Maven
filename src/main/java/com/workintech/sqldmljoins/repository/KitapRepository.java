package com.workintech.sqldmljoins.repository;

import com.workintech.sqldmljoins.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitapRepository extends JpaRepository<Kitap, Long> {

    // 1) Dram ve Hikaye türündeki kitaplar (JOIN yok; alt sorgu)
    // DİKKAT: sıralama kitapno ile; "id" kolonu yok.
    String QUESTION_1 = """
            SELECT k.*
            FROM kitap k
            WHERE k.turno IN (
                SELECT t.turno
                FROM tur t
                WHERE t.ad IN ('Dram','Hikaye')
            )
            ORDER BY k.kitapno
            """;
    @Query(value = QUESTION_1, nativeQuery = true)
    List<Kitap> findBooks();

    // 10) Kitapların ortalama puanı
    String QUESTION_10 = """
            SELECT AVG(puan) FROM kitap
            """;
    @Query(value = QUESTION_10, nativeQuery = true)
    Double findAvgPointOfBooks();
}
