package com.github.sebastian.toepfer.pdf.test.hamcrest;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
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

/**
 *
 * @author sebastian
 */
class ReferenceFinder implements ICOSVisitor {

    private final String referenceName;

    public ReferenceFinder(final COSName reference) {
        this(Objects.requireNonNull(reference).getName());
    }

    public ReferenceFinder(final String referenceName) {
        this.referenceName = Objects.requireNonNull(referenceName);
    }

    @Override
    public Object visitFromArray(final COSArray obj) throws IOException {
        Object result = null;
        for (COSBase base : obj) {
            result = base.accept(this);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Object visitFromBoolean(final COSBoolean obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromDictionary(final COSDictionary obj) throws IOException {
        Object result = null;
        for (Map.Entry<COSName, COSBase> entry : obj.entrySet()) {
            if (!"Kids".equals(entry.getKey().getName())) {
                if (referenceName.equals(entry.getKey().getName())) {
                    result = entry.getValue();
                    break;
                } else {
                    result = entry.getValue().accept(this);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object visitFromDocument(final COSDocument obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromFloat(final COSFloat obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromInt(final COSInteger obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromName(final COSName obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromNull(final COSNull obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromStream(final COSStream obj) throws IOException {
        return null;
    }

    @Override
    public Object visitFromString(final COSString obj) throws IOException {
        return null;
    }

}
