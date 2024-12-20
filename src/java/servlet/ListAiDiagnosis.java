package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ListAiDiagnosis", urlPatterns = {"/ListAiDiagnosis"})
public class ListAiDiagnosis extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String[] symptoms = request.getParameterValues("symptom[]");
        String symptom1 = "", symptom2 = "", symptom3 = "", symptom4 = "", symptom5 = "";
        RequestDispatcher dispatcher;

        for (int i = 0; i < 5; i++) {
            if (i < symptoms.length) {
                switch (i) {
                    case 0 : symptom1 = symptoms[i];
                    case 1 : symptom2 = symptoms[i];
                    case 2 : symptom3 = symptoms[i];
                    case 3 : symptom4 = symptoms[i];
                    case 4 : symptom5 = symptoms[i];
                }
            }
        }

        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("symptom1", symptom1);
        jsonInput.addProperty("symptom2", symptom2);
        jsonInput.addProperty("symptom3", symptom3);
        jsonInput.addProperty("symptom4", symptom4);
        jsonInput.addProperty("symptom5", symptom5);

        String apiUrl = "http://127.0.0.1:5000/predict";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseContent.append(responseLine.trim());
            }
        }

        String responseStr = responseContent.toString();
        Gson gson = new Gson();

        JsonObject jsonResponse = gson.fromJson(responseStr, JsonObject.class);
        String predictedDisease = jsonResponse.has("disease") ? jsonResponse.get("disease").getAsString() : "";

        model.AiDiagnosis aiDiagnosis = new model.AiDiagnosis(
                symptom1.replace("_", " "), 
                symptom2.replace("_", " "), 
                symptom3.replace("_", " "), 
                symptom4.replace("_", " "), 
                symptom5.replace("_", " "), 
                predictedDisease
        );

        session.setAttribute("aiDiagnosis", aiDiagnosis);

        StringBuilder symptomsDisplay = new StringBuilder();
        if (!symptom1.isEmpty()) {
            symptomsDisplay.append(aiDiagnosis.getSymptom1());
        }
        if (!symptom2.isEmpty()) {
            symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom2() : aiDiagnosis.getSymptom2());
        }
        if (!symptom3.isEmpty()) {
            symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom3() : aiDiagnosis.getSymptom3());
        }
        if (!symptom4.isEmpty()) {
            symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom4() : aiDiagnosis.getSymptom4());
        }
        if (!symptom5.isEmpty()) {
            symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom5() : aiDiagnosis.getSymptom5());
        }

        response.sendRedirect("payment.jsp");
    }
}
