package id.ac.ui.cs.advprog.transaksipenjualan.controller;

import id.ac.ui.cs.advprog.transaksipenjualan.model.Transaction;
import id.ac.ui.cs.advprog.transaksipenjualan.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService service;

    @GetMapping("/create")
    public String createTransactionPage(Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        return "CreateTransaction";
    }

    @PostMapping("/create")
    public String createTransactionPost(@ModelAttribute Transaction transaction, Model model) {
        service.create(transaction);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String transactionListPage(Model model) {
        List<Transaction> allTransaction = service.findAll();
        model.addAttribute("transactions", allTransaction);
        return "TransactionList";
    }
}
