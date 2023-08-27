import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameProgress oneProgress = new GameProgress(100, 15, 3, 100.15);
        GameProgress twoProgress = new GameProgress(85, 26, 11, 500.35);
        GameProgress threeProgress = new GameProgress(62, 48, 25, 800.05);
        saveGame("C://Games/savegames/save1.dat", oneProgress);
        saveGame("C://Games/savegames/save2.dat", twoProgress);
        saveGame("C://Games/savegames/save3.dat", threeProgress);

        List<String> listDir = new ArrayList<>();
        listDir.add("C://Games/savegames/save1.dat");
        listDir.add("C://Games/savegames/save2.dat");
        listDir.add("C://Games/savegames/save3.dat");

        zipFiles("C://Games/savegames/savegames.zip", listDir);

        openZip("C://Games/savegames/savegames.zip", "C://Games/savegames");
        GameProgress saveProgress = openProgress("C://Games/savegames/save1.dat");
        GameProgress saveProgress2 = openProgress("C://Games/savegames/save2.dat");
        GameProgress saveProgress3 = openProgress("C://Games/savegames/save3.dat");

        System.out.println(saveProgress.toString());
        System.out.println(saveProgress2.toString());
        System.out.println(saveProgress3.toString());
    }

    public static void saveGame(String directory, GameProgress safe) {
        try (FileOutputStream fos = new FileOutputStream(directory); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(safe);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String directory, List<String> directories) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(directory))) {
            int count = 1;
            for (String dir : directories) {
                FileInputStream fis = new FileInputStream(dir);
                zout.putNextEntry(new ZipEntry("save" + count + ".dat"));
                byte[] byffer = new byte[fis.available()];
                fis.read(byffer);
                zout.write(byffer);
                zout.closeEntry();
                count++;
                fis.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (String dir : directories) {
            File fileDel = new File(dir);
            if (fileDel.delete()) {
                System.out.println(dir + " файл удален");
            } else System.out.println(dir + " файл не удален");
        }
    }

    public static void openZip(String dirzip, String dir) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(dirzip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = dir + "/" + entry.getName();

                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String dir) {
        GameProgress pull = null;
        try (FileInputStream fis = new FileInputStream(dir);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            pull = (GameProgress) ois.readObject();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return pull;
    }
}