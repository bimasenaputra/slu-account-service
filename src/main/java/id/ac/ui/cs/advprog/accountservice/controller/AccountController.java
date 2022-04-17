package id.ac.ui.cs.advprog.accountservice.controller;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController implements Serializable {
    @Autowired
    private AccountService userAccService;

    @GetMapping("/getUsers")
    @ResponseBody
    public ResponseEntity<List<Account>> getUsers() {
        return ResponseEntity.ok(userAccService.getAllUsers());
    }

    @PostMapping(produces = {"application/json"}, path="/createAccount")
    @ResponseBody
    public ResponseEntity postUserAcc(@RequestBody Account user) {
        return ResponseEntity.ok(userAccService.createUser(user));
    }

    @GetMapping(path = "/getUser/{username}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity getUserAcc(@PathVariable(value = "username") String username) {
        Account user = userAccService.getUser(username);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/getUserByEmail/{email}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity getUserByEmail(@PathVariable(value = "email") String email) {
        Account user = userAccService.getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }
}

