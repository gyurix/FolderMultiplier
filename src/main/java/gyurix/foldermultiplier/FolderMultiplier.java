package gyurix.foldermultiplier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class FolderMultiplier {
  private static final Charset charset = Charset.forName("UTF-8");

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("===================================================");
    System.out.println("===   Folder multiplier - by gyuriX - v 1.0.0   ===");
    System.out.println("===================================================");
    System.out.print("Roof folder: ");
    String folder = scanner.nextLine();
    System.out.println("Multiplying, please wait...");
    Path src = new File(folder + File.separator + "week1-1").toPath();
    for (int i = 1; i <= 12; ++i) {
      for (int j = 1; j <= 2; ++j) {
        if (i == 1 && j == 1)
          continue;
        try {
          Files.walkFileTree(src, new CopyFileVisitor(i + "-" + j));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("Finished folder multiplication.");
  }

  public static class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private String to;

    public CopyFileVisitor(String to) {
      this.to = to;
    }

    public void fileBodyReplace(File folder, String from, String to) {
      if (!folder.toString().contains(File.separator + ".")) {
        try {
          int len = (int) folder.length();
          byte[] buf = new byte[len];
          FileInputStream fis = new FileInputStream(folder);
          fis.read(buf);
          fis.close();
          FileOutputStream fos = new FileOutputStream(folder);
          fos.write(new String(buf, charset).replace(from, to).getBytes(charset));
          fos.close();
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
      Files.createDirectories(new File(dir.toFile().getAbsoluteFile().toString().replace("1-1", to)).toPath());
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file,
                                     final BasicFileAttributes attrs) throws IOException {
      File toFile = new File(file.toFile().getAbsoluteFile().toString().replace("1-1", to));
      Files.copy(file, toFile.toPath());
      fileBodyReplace(toFile, "1-1", to);
      return FileVisitResult.CONTINUE;
    }
  }
}
