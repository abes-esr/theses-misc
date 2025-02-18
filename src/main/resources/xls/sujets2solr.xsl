<?xml version="1.0" encoding="UTF-8"?>
<!-- OMZ 08/2012 ajout champ melPro, melPerso, Contrat doctoral, Pub soutenance, Cifre partenaire de recherche-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mets="http://www.loc.gov/METS/"
    xmlns:tef="http://www.abes.fr/abes/documents/tef"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:suj="http://www.theses.fr/namespace/sujets"
    version="1.0">
    <xsl:include href="SGSujets2solr.xsl"/>
    <xsl:output indent="yes"/>
    <xsl:param name="idDeLaBase"/>
    <xsl:template match="/"> 
        <add>
            <doc>
                <xsl:variable name="anom">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom"/>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:prenom"/>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:dateNaissance"/>
                </xsl:variable>
                
                <xsl:variable name="bnom">
                    <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance/text())>0">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance"/>
                    </xsl:if>
                    <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance/text())=0">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom"/>
                    </xsl:if>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:prenom"/>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:dateNaissance"/>
                </xsl:variable>
                
                <!-- idxdoublon -->             
                <field name="idxadoublon">
                    <xsl:call-template name="UPPER">
                        <xsl:with-param name="text">
                            <xsl:value-of select="$anom"></xsl:value-of>
                        </xsl:with-param>
                    </xsl:call-template>
                </field>
                
                <field name="idxbdoublon">
                    <xsl:call-template name="UPPER">
                        <xsl:with-param name="text">
                            <xsl:value-of select="$bnom"></xsl:value-of>
                        </xsl:with-param>
                    </xsl:call-template>
                </field>
			
		  <!-- tous les ppn -->	
		  <xsl:for-each select="//tef:autoriteExterne[@autoriteSource='Sudoc']">                    
					<field name="ppn">
                        <xsl:value-of select="text()"/>
                    </field>
          </xsl:for-each>
          <xsl:for-each select="//tef:elementdEntree[@autoriteSource='Sudoc']">                    
					<field name="ppn">
					    <xsl:value-of select="@autoriteExterne"/>
                    </field>
          </xsl:for-each>
		  <xsl:for-each select="//tef:subdivision[@autoriteSource='Sudoc']">                    
					<field name="ppn">
					    <xsl:value-of select="@autoriteExterne"/>
                    </field>
          </xsl:for-each>
		  
		  <!--
		  <xsl:for-each select="//tef:autoriteExterne[@autoriteSource='Sudoc']">  
			<xsl:if test="text()!=''">                  
		      		<field name="ppn">
       	                 <xsl:value-of select="text()"/>
	                    </field>
		  	</xsl:if>
          </xsl:for-each>

		  <xsl:for-each select="//tef:elementdEntree[@autoriteSource='Sudoc']/@autoriteExterne">                    
		      <xsl:if test="text()!=''">                  
		      		<field name="ppn">
       	                 <xsl:value-of select="text()"/>
	                    </field>
		  	</xsl:if>
          </xsl:for-each>

		  <xsl:for-each select="//tef:subdivision[@autoriteSource='Sudoc']/@autoriteExterne">                    
		      <xsl:if test="text()!=''">                  
		      		<field name="ppn">
       	                 <xsl:value-of select="text()"/>
	                </field>
		  	</xsl:if>
          </xsl:for-each>
		  -->
	
                <!-- id -->             
                <field name="id">
                    <xsl:value-of select="$idDeLaBase"/>
                </field>
                
                <field name="numAuto">
                    <xsl:value-of select="$idDeLaBase"/>
                </field>
                
                <!-- titre en franais -->
                <field name="titre">
                    <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:title"/>
                </field>                                
                
                <!-- titre en langue trangre -->
                <field name="titre2">
                    <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dcterms:alternative"/>
                </field>
                
                <!-- auteur -->
                <field name="auteurNom">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom"/>
                </field>
                <field name="auteurNomNaissance">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance"/>
                </field>
                <field name="auteurPrenom">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:prenom"/>
                </field>
                <field name="auteurNationalite">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nationalite"/>
                </field>
                <field name="auteurDateNaissance_dt">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:dateNaissance"/>
                     <xsl:text>T23:59:59Z</xsl:text>
                </field>
                <field name="auteurDateNaissance">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:dateNaissance"/>
                </field>
                <field name="auteur">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom"/>
                    
                    <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance != /mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom">
                        <xsl:text>,</xsl:text>
                        <xsl:text>(</xsl:text>
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nomDeNaissance"/>
                        <xsl:text>)</xsl:text>
                    </xsl:if>
                    
                    <xsl:text>,</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:prenom"/>
                    <xsl:text>,</xsl:text>
                    <xsl:text>[</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                    <xsl:text>,</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:dateNaissance"/>
                    <xsl:text>,</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nationalite"/>
                    <xsl:text>]</xsl:text>
                </field>
                
                <!-- intervenant : directeur these -->  
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:directeurThese">
                    <field name="intervenant">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                        <xsl:if test="tef:autoriteExterne[@autoriteSource='Sudoc']/text()!=''">
                            <xsl:text>,</xsl:text>
                            <xsl:text>[</xsl:text>
                            <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']/text()"/>
                            <xsl:text>]</xsl:text>
                        </xsl:if>
                    </field>
                    <field name="directeurThese">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                    </field>	
                </xsl:for-each>
                <field name="directeursThese">
                    <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:directeurThese">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                        <xsl:if test="position()&lt;count(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:directeurThese)">
                            <xsl:text>,</xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                </field>
                
                <!-- établissement de soutenance -->
                <field name="etabSoutenance">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor/tef:nom"/>
                </field>
                
                <!-- co-tutelle -->
                <field name="coTutelle">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[2]/tef:nom"/>
                </field>
                
                <field name="coTutelleAut">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[2]/tef:nom"/>
                    <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[2]/tef:autoriteExterne[@autoriteSource='Sudoc']/text()!=''">
                        <xsl:text>,</xsl:text>
                        <xsl:text>[</xsl:text>
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[2]/tef:autoriteExterne[@autoriteSource='Sudoc']/text()"/>
                    <xsl:text>]</xsl:text>
                    </xsl:if>
                </field>
                
                <!-- ecoles doctorales -->
                <field name="ecoleDoctorale">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:ecoleDoctorale/tef:nom"/> 
                </field>
                
                <field name="ecoleDoctoraleAut">
                   <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:ecoleDoctorale/tef:nom"/> 
                        <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:ecoleDoctorale/tef:autoriteExterne[@autoriteSource='Sudoc']/text()!=''">
                            <xsl:text>,</xsl:text>
                            <xsl:text>[</xsl:text>
                            <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:ecoleDoctorale/tef:autoriteExterne[@autoriteSource='Sudoc']/text()"/>
                            <xsl:text>]</xsl:text>
                        </xsl:if>
                </field>
                
                <!-- partenaires de recherche -->
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/tef:nom">
                    <field name="partRecherche">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>
                
                <!-- types partenaire de recherche -->      
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/@type">
                    <field name="typePartRecherche">
                        <xsl:value-of select="."/>
                    </field>
                </xsl:for-each>
                
                <!-- cifre partenaire de recherche -->      
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/suj:cifre">
                    <field name="cifrePartRecherche">
                        <xsl:value-of select="."/>
                    </field>
                </xsl:for-each>
                
                <!-- partenaire -->       
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche">
                    <field name="partenaire">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="@type"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="suj:cifre"/>
                    </field>
                </xsl:for-each>
                
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche">
                    <field name="partenaireAut">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="@type"/>
                        <xsl:text>,</xsl:text>
                        <xsl:if test="@type='autreType'">
                            <xsl:value-of select="@autreType"/>
                            <xsl:text>,</xsl:text>
                        </xsl:if>
                        <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/tef:autoriteExterne[@autoriteSource='Sudoc']/text()!=''">
                            <xsl:text>[</xsl:text>
                            <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/tef:autoriteExterne[@autoriteSource='Sudoc']/text()"/>
                            <xsl:text>]</xsl:text>
                        </xsl:if>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="suj:cifre"/>
                    </field>
                </xsl:for-each>
                
                <!-- date de soutenance -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dcterms:dateAccepted/text()!=''">
                    <field name="dateSoutenance_dt">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dcterms:dateAccepted"/>
                        <xsl:text>T23:59:59Z</xsl:text>
                    </field>
                </xsl:if>
                
                <!-- oaisets -->          
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:oaiSetSpec">
                    <field name="oaiSetSpec">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>
                
                <!-- discipline -->   
                <field name="discipline">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.discipline"/>
                </field>
                
                <!-- subjects en français -->   
                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:subject[@xml:lang='fr']">
                    <field name="subjectFR">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>
                
                <!-- subjects en anglais -->  
                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:subject[@xml:lang='en']">
                    <field name="subjectEN">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>
                
                <!-- abstract en français -->  
                <field name="abstractFR">
                    <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dcterms:abstract[@xml:lang='fr']"/>
                </field>
                
                <!-- abstract en anglais -->  
                <field name="abstractEN">
                    <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dcterms:abstract[@xml:lang='en']"/>
                </field>
                
                <!-- derrogation 10 ans -->
                <xsl:if test=" /mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/@derogationDixAns!=''">
                    <field name="derogationDixAns">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/@derogationDixAns"/>
                    </field>
                </xsl:if>   
                
                <!-- date 1ere inscription doct -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:datePremiereInscriptionDoctorat/text()!=''">
                    <field name="datePremiereInscriptionDoctorat_dt">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:datePremiereInscriptionDoctorat"/>
                        <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:datePremiereInscriptionDoctorat/text())!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                        </xsl:if>
                    </field>
                </xsl:if>
                
                <!-- annee 1ere inscription doct -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:datePremiereInscriptionDoctorat/text()!=''">
                    <field name="anneePremiereInscriptionDoctorat">
                        <xsl:value-of select="substring(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:datePremiereInscriptionDoctorat,0,5)"/>
                    </field>
                </xsl:if>
                
                <!-- date  inscription etab -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:dateInscriptionEtab/text()!=''">
                    <field name="dateInscriptionEtab_dt">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:dateInscriptionEtab"/>
                        <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:dateInscriptionEtab/text())!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                        </xsl:if>   
                    </field>
                </xsl:if>
                
                <!--  contrat doctoral -->  
                <field name="contratDoctoral">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/suj:contratDoctoral"/>
                </field>
                
                <!-- date de soutenance prevue -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue/text()!=''">
                    <field name="dateSoutenancePrevue_dt">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue"/>
                        <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue/text())!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                        </xsl:if>
                    </field>
                </xsl:if>   
                
                <!--  publicité soutenance -->  
                <field name="pubSoutenance">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:publiciteSoutenance"/>
                </field>
                
                <!-- date  abandon -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:dateAbandon/text()!=''">
                    <field name="dateAbandon_dt">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:dateAbandon"/>
                        <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:dateAbandon/text())!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                         </xsl:if>
                    </field>
                </xsl:if>
                
                <!-- date  creation -->
                <xsl:if test="/mets:mets/mets:metsHdr/@CREATEDATE!=''">
                    <field name="dateCreation_dt">
                        <xsl:value-of select="mets:mets/mets:metsHdr/@CREATEDATE"/>
                        <xsl:if test="string-length(mets:mets/mets:metsHdr/@CREATEDATE)!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                         </xsl:if>
                    </field>
                </xsl:if>
               
                <!-- date  modification -->
                <xsl:if test="/mets:mets/mets:metsHdr/@LASTMODDATE!=''">
                    <field name="dateModification_dt">
                        <xsl:value-of select="mets:mets/mets:metsHdr/@LASTMODDATE"/>
                        <xsl:if test="string-length(mets:mets/mets:metsHdr/@LASTMODDATE)!=20">
                            <xsl:text>T23:59:59Z</xsl:text>
                        </xsl:if>
                    </field>
                </xsl:if>
                
                <!-- email pro -->
                <!-- <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='mailPro']!=''"> -->
                    <field name="melPro">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='mailPro']"/>
                    </field>
                <!-- </xsl:if> -->
                
                <!-- email perso -->
                <!-- <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='mailPerso']!=''"> -->
                    <field name="melPerso">
                        <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='mailPerso']"/>
                    </field>
                <!-- </xsl:if> -->
                
                <!-- Données de gestion -->
	     <xsl:call-template name="traiterSujetsGestion"/>

            </doc>
        </add>
    </xsl:template>

	
    <xsl:variable name="accent1" select="'a b c d e f g h i j k l m n o p q r s t u v w x y z À à Á á Â â Ã ã Ä ä Å å Æ æ'" />
    <xsl:variable name="maju1" select="'A B C D E F G H I J K L M N O P Q R S T U V W X Y Z A A A A A A A A A A A A A A'" />
    <xsl:variable name="accent2" select="'Ç ç È è É é Ê ê Ë ë Ì ì Í í Î î Ï ï Ð ð Ñ ñ Ò ò Ó ó Ô ô Õ õ Ö ö Ø ø Ù ù Ú ú Û û Ü ü Ý ý Þ þ'" />
    <xsl:variable name="maju2" select="'C C E E E E E E E E I I I I I I I I D D N N O O O O O O O O O O Ø Ø U U U U U U U U Y Y Þ Þ'" />


    <xsl:template name="UPPER">
        <xsl:param name="text"/>
        <xsl:variable name="tmp1" select="translate(translate(translate($text, $accent1, $maju1),'-',''),' ','')"/>
        <xsl:value-of select="translate($tmp1, $accent2, $maju2)"/>
    </xsl:template>
    
</xsl:stylesheet>
