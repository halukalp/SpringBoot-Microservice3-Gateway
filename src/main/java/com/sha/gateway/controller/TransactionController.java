package com.sha.gateway.controller;

import com.google.gson.JsonElement;
import com.sha.gateway.model.service.AbstractTransactionService;
import com.sha.gateway.security.model.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gateway/transaction")
public class TransactionController
{
    @Autowired
    private AbstractTransactionService service;

    /*
        Oturum acan kullanicinin tum islemlerini(transactions) listelemek icin
        filtreler isteklerden once calisir

        @AuthenticationPrincipal ile oturum acan kullaniciya
        Controller'dan kolayca erisilir.
     */
    @GetMapping
    public ResponseEntity<?> getAllTransactionsOfAuthorizedUser(@AuthenticationPrincipal UserPrinciple userPrinciple)
    {
        return ResponseEntity.ok(service.getAllTransactionsOfUser(userPrinciple.getId()));
    }

    @DeleteMapping("{transactionID}")
    public ResponseEntity<?> delete(@PathVariable Integer transactionID)
    {
        service.deleteByID(transactionID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody JsonElement transaction)
    {
        return ResponseEntity.ok(service.save(transaction));
    }
}
