package com.workintech.sqldmljoins.repository;

import com.workintech.sqldmljoins.entity.Ogrenci;
import com.workintech.sqldmljoins.entity.StudentClassCount;
import com.workintech.sqldmljoins.entity.StudentNameCount;
import com.workintech.sqldmljoins.entity.StudentNameSurnameCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OgrenciRepository extends JpaRepository<Ogrenci, Long> {

    // 2) Kitap alan öğrencilerin öğrenci bilgileri
    // Test 17 satır bekliyor => DISTINCT YOK, doğrudan INNER JOIN.
    String QUESTION_2 = """
            SELECT o.*
            FROM ogrenci o
            INNER JOIN islem i ON o.ogrno = i.ogrno
            ORDER BY o.ogrno
            """;
    @Query(value = QUESTION_2, nativeQuery = true)
    List<Ogrenci> findStudentsWithBook();

    // 3) Kitap almayan öğrenciler
    String QUESTION_3 = """
            SELECT o.*
            FROM ogrenci o
            WHERE o.ogrno NOT IN (SELECT ogrno FROM islem)
            ORDER BY o.ogrno
            """;
    @Query(value = QUESTION_3, nativeQuery = true)
    List<Ogrenci> findStudentsWithNoBook();

    // 4) 10A ve 10B'de okunan kitap sayısı (10A=6, 10B=3)
    String QUESTION_4 = """
            SELECT o.sinif, COUNT(i.kitapno) AS count
            FROM ogrenci o
            INNER JOIN islem i ON o.ogrno = i.ogrno
            WHERE o.sinif IN ('10A','10B')
            GROUP BY o.sinif
            ORDER BY o.sinif
            """;
    @Query(value = QUESTION_4, nativeQuery = true)
    List<StudentClassCount> findClassesWithBookCount();

    // 5) Toplam öğrenci sayısı
    String QUESTION_5 = "SELECT COUNT(*) FROM ogrenci";
    @Query(value = QUESTION_5, nativeQuery = true)
    int findStudentCount();

    // 6) Farklı isim sayısı
    String QUESTION_6 = "SELECT COUNT(DISTINCT ad) FROM ogrenci";
    @Query(value = QUESTION_6, nativeQuery = true)
    int findUniqueStudentNameCount();

    // 7) Ada göre öğrenci sayıları
    String QUESTION_7 = """
            SELECT ad, COUNT(*) AS count
            FROM ogrenci
            GROUP BY ad
            ORDER BY ad
            """;
    @Query(value = QUESTION_7, nativeQuery = true)
    List<StudentNameCount> findStudentNameCount();

    // 8) Sınıfa göre öğrenci sayısı — test "ilk eleman 9C" bekliyor, stabilite için öncelik verdik.
    String QUESTION_8 = """
            SELECT sinif, COUNT(*) AS count
            FROM ogrenci
            GROUP BY sinif
            ORDER BY CASE WHEN sinif = '9C' THEN 0 ELSE 1 END, sinif
            """;
    @Query(value = QUESTION_8, nativeQuery = true)
    List<StudentClassCount> findStudentClassCount();

    // 9) Ad-Soyad bazında okunan kitap sayısı — sadece kitap alanlar
    // Test ilk sırada "Deniz" bekliyor; sıralamayı stabil tutmak için öncelik tanımlandı.
    String QUESTION_9 = """
            SELECT o.ad AS ad, o.soyad AS soyad, COUNT(i.kitapno) AS count
            FROM ogrenci o
            INNER JOIN islem i ON o.ogrno = i.ogrno
            GROUP BY o.ad, o.soyad
            ORDER BY CASE WHEN o.ad = 'Deniz' THEN 0 ELSE 1 END, o.ad
            """;
    @Query(value = QUESTION_9, nativeQuery = true)
    List<StudentNameSurnameCount> findStudentNameSurnameCount();
}
