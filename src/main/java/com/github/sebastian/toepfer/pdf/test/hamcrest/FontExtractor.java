package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.io.IOException;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.ICOSVisitor;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;

/**
 *
 * @author sebastian
 */
class FontExtractor implements ICOSVisitor {

    @Override
    public Object visitFromArray(final COSArray obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromBoolean(final COSBoolean obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromDictionary(final COSDictionary obj) throws IOException {
        final PDFont result;
        if (obj.containsKey(COSName.SUBTYPE)) {
            result = createAsFont(obj.getCOSName(COSName.SUBTYPE), obj);
        } else {
            result = null;
        }
        return result;
    }

    private PDFont createAsFont(final COSName subType, final COSDictionary obj) throws IOException {
        PDFont result;
        if (COSName.TYPE0.equals(subType)) {
            result = new PDType0Font(obj);
        } else if (COSName.TYPE1.equals(subType)) {
            result = new PDType1Font(obj);
        } else if (COSName.TYPE3.equals(subType)) {
            result = new PDType3Font(obj);
        } else if (COSName.TRUE_TYPE.equals(subType)) {
            result = new PDTrueTypeFont(obj);
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public Object visitFromDocument(final COSDocument obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromFloat(final COSFloat obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromInt(final COSInteger obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromName(final COSName obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromNull(final COSNull obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromStream(final COSStream obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFromString(final COSString obj) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
