package com.esen.bookstore.shell;

import com.esen.bookstore.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("Bookstore related commands")
@RequiredArgsConstructor
public class BookstoreHandler {

    private final BookstoreService bookstoreService;

    @ShellMethod(key = "create bookstore", value = "Create a new bookstore")
    public void save(String location, Double priceModifier, Double moneyInCashRegister) {
        bookstoreService.save(location, priceModifier, moneyInCashRegister);
    }

    @ShellMethod(key = "list bookstores", value = "list bookstores")
    public String list(){
        return bookstoreService.findAll().stream()
                .map(bookstore -> "ID: %d, priceModifier: %f, moneyInCashRegister: %f, location: %s".formatted(
                        bookstore.getId(),
                        bookstore.getPriceModifier(),
                        bookstore.getMoneyInCashRegister(),
                        bookstore.getLocation()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(key = "delete bookstore", value = "delete bookstore")
    public void deleteBookstore(Long id) {
        bookstoreService.deleteBookstore(id);
    }

    @ShellMethod(key = "update bookstore", value = "Updates bookstore")
    public void updateBookstore(Long id,
                                @ShellOption(defaultValue = ShellOption.NULL) String location,
                                @ShellOption(defaultValue = ShellOption.NULL) Double priceModifier,
                                @ShellOption(defaultValue = ShellOption.NULL) Double moneyInCashRegister) {
        bookstoreService.updateBookstore(id, location, priceModifier, moneyInCashRegister);
    }

    @ShellMethod(key = "get stock", value = "get stock")
    public String getStock(Long id) {
        return bookstoreService.getStock(id).entrySet()
                .stream()
                .map(entry -> "Book ID: %s, Author: %s, Title: %s - Copies: %s".formatted(
                        entry.getKey().getId(),
                        entry.getKey().getAuthor(),
                        entry.getKey().getTitle(),
                        entry.getValue()
                )).collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Add stock", key = "add stock")
    public void addStock(Long bookstoreId, Long bookId, int amount) {
        bookstoreService.changeStock(bookstoreId, bookId, amount);
    }
}
