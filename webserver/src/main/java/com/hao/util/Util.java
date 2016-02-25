package com.hao.util;


import java.io.*;
import java.util.zip.GZIPOutputStream;

public class Util {


    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {}
        }
    }

    /**
     *
     * @param file
     * @param zip
     * @return
     * @throws IOException
     */
    public static byte[] file2byteArrary(File file,boolean zip) throws IOException {
       GZIPOutputStream gzip = null;
       InputStream is = null;
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            if(zip){
                gzip = new GZIPOutputStream(baos);
            }
            int read = 0;
            while((read = is.read(buffer)) != -1){
                if(zip){
                    gzip.write(buffer,0,read);
                }else{
                    baos.write(buffer,0,read);
                }
            }
        }catch (IOException e){
            throw e;
        }finally{
            close(gzip);
            close(is);
        }
        return baos.toByteArray();
    }

	
}
