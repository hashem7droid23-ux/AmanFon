package com.example

import com.example.model.ImeiValidator
import com.example.data.MockDataProvider
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testImeiFormatting() {
    val imei = "356938035643803"
    val formatted = ImeiValidator.formatImei(imei)
    assertTrue(formatted.contains("35"))
    assertEquals(18, formatted.length)
  }

  @Test
  fun testMockDataExists() {
    assertTrue(MockDataProvider.initialReports.isNotEmpty())
    val stolenPhone = MockDataProvider.initialReports.find { it.imei1 == "356938035643803" }
    assertNotNull(stolenPhone)
    assertEquals("Apple", stolenPhone?.brand)
  }
}
