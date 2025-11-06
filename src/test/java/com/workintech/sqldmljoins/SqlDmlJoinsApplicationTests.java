package com.workintech.sqldmljoins;

import java.util.Locale;
import com.workintech.sqldmljoins.entity.StudentNameCount;
import com.workintech.sqldmljoins.repository.KitapRepository;
import com.workintech.sqldmljoins.repository.OgrenciRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(ResultAnalyzer.class)
class SqlDmlJoinsApplicationTests {

    static {
        Locale.setDefault(Locale.US);
    }

    private final KitapRepository kitapRepository;
    private final OgrenciRepository ogrenciRepository;

    @Autowired
    public SqlDmlJoinsApplicationTests(KitapRepository kitapRepository, OgrenciRepository ogrenciRepository) {
        this.kitapRepository = kitapRepository;
        this.ogrenciRepository = ogrenciRepository;
    }

    @DisplayName("Dram ve Hikaye türündeki kitapları listeleyin.")
    @Test
    void findBooksTest(){
        assertEquals(3, kitapRepository.findBooks().size());
    }

    @DisplayName("Kitap alan öğrencilerin öğrenci bilgilerini listeleyin.")
    @Test
    void findStudentsWithBookTest(){
        assertEquals(17, ogrenciRepository.findStudentsWithBook().size());
    }

    @DisplayName("Kitap almayan öğrencileri listeleyin.")
    @Test
    void findStudentsWithNoBookTest(){
        assertEquals(2, ogrenciRepository.findStudentsWithNoBook().size());
    }

    @DisplayName("10A ve 10B sınıflarındaki öğrencilerin sınıf ve okuduğu kitap sayısı")
    @Test
    void findClassesWithBookCountTest(){
        assertEquals(2, ogrenciRepository.findClassesWithBookCount().size());
        assertEquals(6, ogrenciRepository.findClassesWithBookCount().get(0).getCount());
    }

    @DisplayName("Öğrenci tablosundaki öğrenci sayısını gösterin.")
    @Test
    void findStudentCountTest(){
        assertEquals(10, ogrenciRepository.findStudentCount());
    }

    @DisplayName("Öğrenci tablosunda kaç farklı isimde öğrenci olduğunu listeleyiniz.")
    @Test
    void findUniqueStudentNameCountTest(){
        assertEquals(9, ogrenciRepository.findUniqueStudentNameCount());
    }

    @DisplayName("İsme göre öğrenci sayılarının adedini bulunuz.")
    @Test
    void findStudentNameCountTest(){
        List<StudentNameCount> studentNameCountList = ogrenciRepository.findStudentNameCount();
        StudentNameCount sema = studentNameCountList.stream()
                .filter(studentNameCount -> studentNameCount.getAd().equals("Sema"))
                .collect(Collectors.toList()).get(0);

        assertEquals(2, sema.getCount());
        assertEquals(9, ogrenciRepository.findStudentNameCount().size());
    }

    @DisplayName("Her sınıftaki öğrenci sayısını bulunuz..")
    @Test
    void findStudentClassCountTest(){
        assertEquals("9C", ogrenciRepository.findStudentClassCount().get(0).getSinif());
        assertEquals(2, ogrenciRepository.findStudentClassCount().get(0).getCount());
        assertEquals(6, ogrenciRepository.findStudentClassCount().size());
    }

    @DisplayName("Her öğrencinin ad soyad karşılığında okuduğu kitap sayısını getiriniz.")
    @Test
    void findStudentNameSurnameCountTest(){
        assertEquals("Deniz", ogrenciRepository.findStudentNameSurnameCount().get(0).getAd());
        assertEquals(8, ogrenciRepository.findStudentNameSurnameCount().size());
    }

    @DisplayName("Tüm kitapların ortalama puanını bulunuz.")
    @Test
    void findAvgPointOfBooksTest(){
        assertEquals("19.42", String.format("%.2f", kitapRepository.findAvgPointOfBooks()));
    }
}

