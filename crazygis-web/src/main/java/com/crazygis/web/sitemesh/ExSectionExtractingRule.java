package com.crazygis.web.sitemesh;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.tagprocessor.BasicBlockRule;
import org.sitemesh.tagprocessor.Tag;

import java.io.IOException;

/**
 * Created by William on 2014/9/29.
 */
public class ExSectionExtractingRule extends BasicBlockRule<String> {

    private final ContentProperty targetProperty;
    private final boolean includeInContent = false;
    private final SiteMeshContext context;

    public ExSectionExtractingRule(SiteMeshContext context, ContentProperty targetProperty){
        this.context = context;
        this.targetProperty = targetProperty;
    }

    @Override
    protected String processStart(Tag tag) throws IOException {

        Tag t = tag;

        if(t.hasAttribute("id",false)){
            // Push a buffer for the OUTER contents.
            if (!includeInContent) {
                // If the tag should NOT be included in the contents, we use a data-only buffer,
                // which means that although the contents won't be written
                // back to the ContentProperty, they will be available in the main Content data.
                // See Content.createDataOnlyBuffer()
                tagProcessorContext.pushBuffer(targetProperty.getOwningContent().createDataOnlyBuffer());
            } else {
                tagProcessorContext.pushBuffer();
            }

            // Write opening tag to OUTER buffer.
            t.writeTo(tagProcessorContext.currentBuffer());

            // Push a new buffer for storing the INNER contents.
            tagProcessorContext.pushBuffer();

            return t.getAttributeValue("id", false);
        }

        return null;
    }

    @Override
    protected void processEnd(Tag tag, String id) throws IOException {
        if(id != null){
            // Get INNER content, and pop the buffer for INNER contents.
            CharSequence innerContent = tagProcessorContext.currentBufferContents();
            tagProcessorContext.popBuffer();

            // Write the INNER content and closing tag, to OUTER buffer and pop it.
            tagProcessorContext.currentBuffer().append(innerContent);
            if (tag.getType() != Tag.Type.EMPTY) { // if the tag is empty we have already written it in processStart().
                tag.writeTo(tagProcessorContext.currentBuffer());
            }
            CharSequence outerContent = tagProcessorContext.currentBufferContents();
            tagProcessorContext.popBuffer();

            // Write the OUTER contents to the current buffer, which is now the buffer before the
            // tag was processed. Note that if !includeInContent, this buffer will not be written
            // to the ContentProperty (though it will be available in Content.getData()).
            // See comment in processStart().
            tagProcessorContext.currentBuffer().append(outerContent);

            // Export the tag's inner contents to
            if (!targetProperty.hasValue()) {
                targetProperty.getChild(id).setValue(innerContent);
            }
        }
    }
}
