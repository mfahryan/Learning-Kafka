package org.example;

import org.apache.avro.Schema;

public class Payment {
    private String id;
    private double amount;

    // Default constructor required by Avro
    public Payment() {
    }

    // Constructor with parameters
    public Payment(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    // Getter and Setter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



    // Add a method to retrieve the Avro schema
    public Schema getSchema() {
        // Define the Avro schema for the Payment record
        String avroSchema = "{"
                + "\"type\":\"record\","
                + "\"name\":\"Payment\","
                + "\"fields\":["
                + "{\"name\":\"id\",\"type\":\"string\"},"
                + "{\"name\":\"amount\",\"type\":\"double\"}"
                + "]}";
        return new Schema.Parser().parse(avroSchema);
    }
}
