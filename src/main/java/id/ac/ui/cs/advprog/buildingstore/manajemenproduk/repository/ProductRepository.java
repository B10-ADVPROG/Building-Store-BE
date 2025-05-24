package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
