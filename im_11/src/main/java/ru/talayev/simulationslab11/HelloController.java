package ru.vorotov.simulationslab11;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.vorotov.simulationslab11.NormalDist.NormalDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloController {
    @FXML
    private TextField meanField;
    @FXML
    private TextField varField;
    @FXML
    private TextField trialsField;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Label averageLabel;
    @FXML
    private Label varLabel;
    @FXML
    private Label chiLabel;

    double[] expected = {0, 3.8, 6, 7.8, 9.5, 11.1, 12.6, 14.1, 15.5, 16.9, 18.3, 19.7, 21.0, 22.4, 23.7, 25, 26.3, 27.6, 28.9, 30.1, 31.4, 32.7, 33.9, 35.2, 36.4, 37.7, 38.9, 40.1, 41.3, 42.6, 43.8};

    public void onStartButtonClick(ActionEvent actionEvent) {
        double numOfEvents = Double.parseDouble(trialsField.getText());
        double mean = Double.parseDouble(meanField.getText());
        double variance = Double.parseDouble(varField.getText());

        List<Double> sample = new ArrayList<>();
        NormalDistribution normalDist = new NormalDistribution(mean, Math.sqrt(variance));

        Random random = new Random();
        for (int i = 0; i < numOfEvents; i++) {
            sample.add(generateNormalRandom(random, mean, Math.sqrt(variance)));
        }

        sample.sort(null);

        double k = findAmountOfBins((int) numOfEvents);
        double step = (sample.get(sample.size() - 1) - sample.get(0)) / k;

        int l = 0;
        List<Integer> frequencies = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            int count = 0;
            while ((l < numOfEvents) && (sample.get(l) <= sample.get(0) + i * step)) {
                l++;
                count++;
            }
            frequencies.add(count);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < frequencies.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), frequencies.get(i) / numOfEvents));
        }
        barChart.getData().clear();
        barChart.getData().add(series);

        double sampleMean = sample.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double sampleVariance = countVar((int) numOfEvents, sample, sampleMean);

        double chi2 = countChi2(frequencies, normalDist, sample, step, numOfEvents);

        averageLabel.setText("Среднее: " + sampleMean + "\tОшибка: " + Math.abs((sampleMean - mean) / mean * 100) + " %");
        varLabel.setText("Дисперсия: " + sampleVariance + "\tОшибка: " + Math.abs((sampleVariance - variance) / variance * 100) + " %");
        chiLabel.setText("Хи-квадрат: " + chi2);

        if (chi2 > expected[(int) k]) {
            chiLabel.setText(" " + chiLabel.getText() + " > " + expected[(int) k] + " нулевая гипотеза отвергнута");
        } else {
            chiLabel.setText(" " + chiLabel.getText() + " < " + expected[(int) k] + " нулевая гипотеза принята");
        }
    }

    private double countChi2(List<Integer> frequencies, NormalDistribution normalDist, List<Double> sample, double step,double numOfEvents) {
        double res = 0;
        for (int i = 0; i < frequencies.size(); i++) {
            double expected = normalDist.cumulativeProbability(sample.get(0) + (i + 1) * step) - normalDist.cumulativeProbability(sample.get(0) + i * step);
            res += Math.pow(frequencies.get(i) - expected * numOfEvents, 2) / (expected * numOfEvents);
        }
        return res;
    }

    private double countVar(int N, List<Double> x, double mean) {
        double res = 0;

        for (int i = 0; i < N; i++) {
            res += x.get(i) * x.get(i);
        }
        res /= N;
        res -= mean * mean;

        return res;
    }

    private double generateNormalRandom(Random random, double mean, double stdDev) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();

        double res = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);

        return mean + res * stdDev;
    }

    private int findAmountOfBins(int n) {
        double res = 2 * Math.pow(n, (double) 1 / 3);
        return (int) res;
    }
}
