package org.openomf;

import android.content.res.AssetManager;

import org.libsdl.app.SDLActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpenomfActivity extends SDLActivity
{
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyAssetsIfNeeded();
    }

    private void copyAssetsIfNeeded() {
        File internalDir = getFilesDir(); // /data/data/<package_name>/files
        File marker = new File(internalDir, "resources/openomf.bk");

        if (!marker.exists()) {
            copyAssetFolder(getAssets(), "resources", new File(internalDir, "resources"));
            copyAssetFolder(getAssets(), "shaders", new File(internalDir, "shaders"));
        }
    }

    private static void copyAssetFolder(AssetManager assetManager, String fromAssetPath, File toDir) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            if (files == null || files.length == 0) {
                copyAssetFile(assetManager, fromAssetPath, toDir);
            } else {
                if (!toDir.exists()) toDir.mkdirs();
                for (String file : files) {
                    copyAssetFolder(assetManager, fromAssetPath + "/" + file, new File(toDir, file));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyAssetFile(AssetManager assetManager, String fromAssetPath, File toFile) throws IOException {
        try (InputStream in = assetManager.open(fromAssetPath);
             OutputStream out = new FileOutputStream(toFile)) {
            byte[] buffer = new byte[1024 * 32];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}
