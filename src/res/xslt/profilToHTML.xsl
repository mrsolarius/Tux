<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:tux="http://myGame/tux" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Dico Tux Letter Game - Profil</title>
            </head>
            <body>
                <h1><xsl:value-of select="/tux:profil/tux:nom/text()"/></h1>
                <h2><xsl:value-of select="/tux:profil/tux:avatar/text()"/></h2>
                <h3><xsl:value-of select="/tux:profil/tux:anniversaire/text()"/></h3>
                
                <table>
                    <thead>
                        <tr>
                            <th>
                                Date
                            </th>
                            <th>
                                Temps
                            </th>
                            <th>
                                Mots
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="//tux:partie"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:partie">
        <tr>
            <td><xsl:value-of select="@date"/></td>
            <td><xsl:value-of select="tux:temps/text()"/></td>
            <td>
                <xsl:apply-templates select="tux:mot">
                        <xsl:sort select="@niveau" order="ascending"/>
                        <xsl:sort select="text()" order="ascending"/>
                </xsl:apply-templates>
            </td>
        </tr>
    </xsl:template>
    
    <xsl:template match="tux:mot">
        <li>
            <strong><xsl:value-of select="@niveau"/></strong> - <xsl:value-of select="text()"/>
        </li>
    </xsl:template>
</xsl:stylesheet>