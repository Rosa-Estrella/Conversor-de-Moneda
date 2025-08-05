package com.miproyecto.Conversorm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el código de la moneda destino (por ejemplo: DOP, EUR, MXN): ");
        String monedaDestino = scanner.nextLine().toUpperCase();

        double tasa = obtenerTasa(monedaDestino);
        System.out.println("La tasa de cambio de USD a " + monedaDestino + " es: " + tasa);
    }

    public static double obtenerTasa(String monedaDestino) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/33c15e9a19cf761886c09254/latest/USD";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonString = response.body();

        JsonElement rootElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = rootElement.getAsJsonObject();

        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        if (!conversionRates.has(monedaDestino)) {
            throw new IllegalArgumentException("Código de moneda no válido: " + monedaDestino);
        }

        return conversionRates.get(monedaDestino).getAsDouble();
    }
}


