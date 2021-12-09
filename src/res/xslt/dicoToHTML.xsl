<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:tux="http://myGame/tux" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Dico Tux Letter Game</title>
            </head>
            <body>
                <h1>Dictionnaire</h1>
                <ul>
                    <xsl:apply-templates select="//tux:mot">
                        <xsl:sort select="@niveau" order="ascending"/>
                        <xsl:sort select="." order="ascending"/>
                    </xsl:apply-templates>
                </ul>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:mot">
        <li>
            <strong><xsl:value-of select="./@niveau"/></strong> - <xsl:value-of select="."/>
        </li>
    </xsl:template>
</xsl:stylesheet>