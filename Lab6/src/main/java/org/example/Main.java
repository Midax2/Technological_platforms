package org.example;

import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String INPUT_DIRECTORY = "C:\\Obrazy\\";
    private static final String OUTPUT_DIRECTORY = "C:\\ObrazyNew\\";
    private static final int THREAD_POOL_SIZE = 4;

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        try {
            try (Stream<Path> paths = Files.list(Path.of(INPUT_DIRECTORY))) {
                List<Pair<String, BufferedImage>> imagePairs = paths
                        .parallel()
                        .map(Main::loadImagePair)
                        .collect(Collectors.toList());
                System.out.println("Wczytano " + imagePairs.size() + " obrazów");

                try (ForkJoinPool customThreadPool = new ForkJoinPool(THREAD_POOL_SIZE)) {
                    customThreadPool.submit(() -> {
                        try {
                            imagePairs.parallelStream()
                                    .map(Main::transformImagePair).filter(Objects::nonNull)
                                    .forEach(Main::saveImage);
                        } catch (Exception e) {
                            System.out.println("Wystąpił błąd podczas przetwarzania obrazów: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }).get();
                }
            }
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas przetwarzania obrazów: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Przetwarzanie wczytanych obrazów zajęło " +
                ((System.currentTimeMillis() - time) / 1000.0) + "s");
    }

    private static Pair<String, BufferedImage> loadImagePair(Path path) {
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            String name = path.getFileName().toString();
            return Pair.of(name, image);
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas wczytywania obrazu: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private static Pair<String, BufferedImage> transformImagePair(Pair<String, BufferedImage> pair) {
        String name = pair.getLeft();
        BufferedImage image = pair.getRight();
        if (image == null) {
            System.out.println("Wystąpił błąd podczas transformacji obrazu: " + name + " - Obraz jest pusty");
            return null;
        }
        BufferedImage transformedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                int red = color.getRed();
                int blue = color.getGreen();
                int green = color.getBlue();
                Color outColor = new Color(red, green, blue);
                transformedImage.setRGB(x, y, outColor.getRGB());
            }
        }

        return Pair.of(name, transformedImage);
    }

    private static void saveImage(Pair<String, BufferedImage> pair) {
        String name = pair.getLeft();
        BufferedImage image = pair.getRight();
        if (image == null) {
            System.out.println("Wystąpił błąd podczas zapisywania obrazu: " + name + " - Obraz po transformacji jest pusty");
            return;
        }
        Path outputPath = Path.of(OUTPUT_DIRECTORY, name);
        try {
            ImageIO.write(image, "png", outputPath.toFile());
            System.out.println("Zapisano obraz: " + outputPath);
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisywania obrazu: " + outputPath);
            e.printStackTrace();
        }
    }
}
