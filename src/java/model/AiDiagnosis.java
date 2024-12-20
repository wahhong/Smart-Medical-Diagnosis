package model;

public class AiDiagnosis {
    private int diagnosisID;
    private String symptom1;
    private String symptom2;
    private String symptom3;
    private String symptom4;
    private String symptom5;
    private String primaryDiagnosis;

    public AiDiagnosis() {
    }

    public AiDiagnosis(int diagnosisID, String symptom1, String symptom2, String symptom3, String symptom4, String symptom5, String primaryDiagnosis) {
        this.diagnosisID = diagnosisID;
        this.symptom1 = symptom1;
        this.symptom2 = symptom2;
        this.symptom3 = symptom3;
        this.symptom4 = symptom4;
        this.symptom5 = symptom5;
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public AiDiagnosis(String symptom1, String symptom2, String symptom3, String symptom4, String symptom5, String primaryDiagnosis) {
        this.symptom1 = symptom1;
        this.symptom2 = symptom2;
        this.symptom3 = symptom3;
        this.symptom4 = symptom4;
        this.symptom5 = symptom5;
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public int getDiagnosisID() {
        return diagnosisID;
    }

    public void setDiagnosisID(int diagnosisID) {
        this.diagnosisID = diagnosisID;
    }

    public String getSymptom1() {
        return symptom1;
    }

    public void setSymptom1(String symptom1) {
        this.symptom1 = symptom1;
    }

    public String getSymptom2() {
        return symptom2;
    }

    public void setSymptom2(String symptom2) {
        this.symptom2 = symptom2;
    }

    public String getSymptom3() {
        return symptom3;
    }

    public void setSymptom3(String symptom3) {
        this.symptom3 = symptom3;
    }

    public String getSymptom4() {
        return symptom4;
    }

    public void setSymptom4(String symptom4) {
        this.symptom4 = symptom4;
    }

    public String getSymptom5() {
        return symptom5;
    }

    public void setSymptom5(String symptom5) {
        this.symptom5 = symptom5;
    }

    public String getPrimaryDiagnosis() {
        return primaryDiagnosis;
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }
    
    

}
