package com.esen.bookstore.shell;

import com.esen.bookstore.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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
}
