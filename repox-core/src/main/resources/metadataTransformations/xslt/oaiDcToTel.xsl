<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'
                version="1.0">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/oai_dc:dc">
        <record xmlns="http://krait.kb.nl/coop/tel/handbook/telterms.html">
            <xsl:copy-of select="*/."/>
        </record>
    </xsl:template>
</xsl:stylesheet>
