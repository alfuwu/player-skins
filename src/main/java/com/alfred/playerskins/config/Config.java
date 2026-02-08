package com.alfred.playerskins.config;

import com.alfred.playerskins.events.init.InitListener;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Excluded { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotWorking { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Comment {
        String[] text();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NewLine {
        int amount() default 1;
    }

    @Comment(text = "Allows slim player models to function correctly")
    public static boolean allowSlim = true;

    @Comment(text = "Allows the second skin layer to properly render")
    public static boolean allowMultipleLayers = true;

    // TODO: impl
    @Excluded
    @NotWorking
    @Comment(text = "Makes first person mode render the entire body rather than just your hand")
    public static boolean realisticFirstPerson = false;

    @Comment(text = "Adds support for modern Minecraft capes to the game")
    public static boolean allowCapes = true;

    public static void save() {
        Field[] fields = Config.class.getDeclaredFields();

        try (OutputStream outputStream = new FileOutputStream(FabricLoader.getInstance().getConfigDir().toString() + "/player-skins.properties")) {
            PrintWriter writer = new PrintWriter(outputStream);
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Excluded.class)) {
                    field.setAccessible(true);
                    String fieldValue = field.get(null).toString();

                    // separate fields with new lines
                    if (field.isAnnotationPresent(NewLine.class))
                        for (int i = 0; i++ < field.getAnnotation(NewLine.class).amount();)
                            writer.println("");

                    // add comment(s)
                    if (field.isAnnotationPresent(Comment.class))
                        for (String string : field.getAnnotation(Comment.class).text())
                            writer.println("# " + string);

                    // write field name and its value
                    writer.println((field.isAnnotationPresent(NotWorking.class) ? '#' : "") + field.getName() + "=" + fieldValue);
                }
            }
            writer.flush();
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Uncaught", e);
        }
    }

    public static void read() {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(FabricLoader.getInstance().getConfigDir().toString() + "/player-skins.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) // don't save if the error wasn't the lack of a file, to avoid deleting existing config values
                save();
            return;
        }

        Field[] fields = Config.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                // get value from properties file and set it to the field
                String value = properties.getProperty(field.getName());
                if (value != null) {
                    if (field.getType().equals(int.class))
                        field.setInt(null, Integer.parseInt(value));
                    else if (field.getType().equals(double.class))
                        field.setDouble(null, Double.parseDouble(value));
                    else if (field.getType().equals(float.class))
                        field.setFloat(null, Float.parseFloat(value));
                    else if (field.getType().equals(boolean.class))
                        field.setBoolean(null, Boolean.parseBoolean(value));
                    else if (field.getType().equals(char.class))
                        field.setChar(null, value.charAt(0));
                    else if (field.getType().equals(byte.class))
                        field.setByte(null, Byte.parseByte(value));
                    else if (field.getType().equals(long.class))
                        field.setLong(null, Long.parseLong(value));
                    else if (field.getType().equals(short.class))
                        field.setShort(null, Short.parseShort(value));
                    else if (field.getType().equals(String.class))
                        field.set(null, value);
                    else if (field.getType().equals(AtomicBoolean.class))
                        field.set(null, new AtomicBoolean(Boolean.parseBoolean(value)));
                    else
                        InitListener.LOGGER.error("Config field \"{}\" could not be parsed. Class: {}", field.getName(), field.getClass().getSimpleName());
                }
            } catch (IllegalAccessException | NumberFormatException e) {
                throw new RuntimeException("Invalid field data", e);
            }
        }
        save();
    }
}