package com.example.wallet_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testDeposit() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType("application/json")
                        .content("""
      {
        "walletId":"11111111-1111-1111-1111-111111111111",
        "operationType":"DEPOSIT",
        "amount":1000
      }
    """))
                .andExpect(status().isOk());
    }
}
