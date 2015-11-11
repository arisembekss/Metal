package com.dtech.orm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ADIST on 10/20/2015.
 */
public class DefaultOps {
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DEFAULT_DATETIME_MILI2ND_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final int DEFAULT_COMPRESSION = 60;
    public static final String DEFAULT_IMAGE_EXT = ".jpg";
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PERM_GET_ACCOUNTS = 2;
    public static final String KEY_IS_RESOLVING = "is_resolve";
    public static final String KEY_SHOULD_RESOLVE = "should_resolve";
    //URL untuk ambil data dari json file
    public static final String URL_CUSTOMER = "http://droidsense.web.id/metal/customer.json";
    public static final String EMPTY_STRING = "";
    public static final String IMAGE_DIRECTORY_NAME = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MetalImages/";

    public static String encodeImage(Bitmap image, Bitmap.CompressFormat compresFormat
            , int compressQuality){
        if (compresFormat == null)
            compresFormat = DEFAULT_COMPRESS_FORMAT;
        if (compressQuality < 60)
            compressQuality = DEFAULT_COMPRESSION;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(compresFormat, compressQuality, stream);
        byte[] imageByteArray = stream.toByteArray();
        // code below still left a log like this shit:
        // -->> W/OpenGLRenderer: Bitmap too large to be uploaded into a texture
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String base64ImgString){
        byte[] decodedImage = Base64.decode(base64ImgString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedImage,
                0, decodedImage.length);
    }

    public static String dateToString(Date date, String format) {
        if (format == null || format.length() <= 0)
            format = DEFAULT_DATETIME_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateToString(Calendar calendar, String format) {
        return dateToString(calendar.getTime(), format);
    }

    public static float[] getLocationRef(String imagePath) throws IOException {
        ExifInterface exif = new ExifInterface(imagePath);
        String[] result = {
                exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF),
                exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF),
                exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
                exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
        };
        boolean pass = true;
        for (String res : result){
            if (res == null || res.length() <= 0){
                pass = false;
                break;
            }
        }

        if (!pass){
            return new float[]{};
        }
        float latitude = convertToDegree(result[2]);
        float longitude = convertToDegree(result[3]);

        latitude = result[0].equals("N") ? latitude : -latitude;
        longitude = result[1].equals("E") ? longitude : -longitude;

        return new float[] {longitude, latitude};
    }

    private static float convertToDegree(String coordinat) {
        Float result;
        String[] DMS = coordinat.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
        return result;
    }
	
	public static String setLocationRef(String imagePath, float[] coord) {
		try {
			// coord[0] : latitude, coord[1] : longitude
			ExifInterface exif = new ExifInterface(imagePath);
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convertFromDegree(coord[0]);
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convertFromDegree(coord[1]);
			String latRef = coord[0] > 0 ? "N" : "S";
			String lonRef = coord[1] > 0 ? "E" : "W";
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latRef);
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lonRef);
			exif.saveAttributes();	
			return imagePath;
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private static String convertFromDegree(float coord) {
		// http://stackoverflow.com/questions/10531544/write-geotag-jpegs-exif-data-in-android
        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
		String sOut = Integer.toString((int)coord) + "/1,";   // 105/1,
		coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
		sOut = sOut + Integer.toString((int)coord) + "/1,";   // 105/1,59/1,
		coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
		sOut = sOut + Integer.toString((int)coord) + "/1000";   // 105/1,59/1,15555/1000
		return sOut;
    }
}
