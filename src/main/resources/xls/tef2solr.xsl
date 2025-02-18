<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mets="http://www.loc.gov/METS/"
    xmlns:metsRights="http://cosimo.stanford.edu/sdr/metsrights/"
    xmlns:tef="http://www.abes.fr/abes/documents/tef"
    xmlns:tefextension="http://www.abes.fr/abes/documents/tefextension"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:suj="http://www.theses.fr/namespace/sujets"
    version="1.0">
    <xsl:include href="SG2solr.xsl"/>
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
                
                <!-- id -->

                <field name="id">
                    <xsl:value-of select="$idDeLaBase"/>
                </field>

                <!-- titre en français -->

                <field name="titre">
                    <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:title"/>
                </field>                                

                <!-- titre en langue étrangère -->

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
                <field name="auteurPpn">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                </field>
                <field name="auteurNationalite">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nationalite"/>
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


                <!-- intervenant : membres du jury -->
                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:membreJury">
                    <field name="intervenant">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:text>[</xsl:text>
                        <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                        <xsl:text>]</xsl:text>
                    </field>
		    <field name="membreJury">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                    </field>
                </xsl:for-each>

                <!-- intervenant : president du jury -->

                <field name="intervenant">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:presidentJury/tef:nom"/>
                    <xsl:text>,</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:presidentJury/tef:prenom"/>
                    <xsl:text>,</xsl:text>
                    <xsl:text>[</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:presidentJury/tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                    <xsl:text>]</xsl:text>
                </field>
		<field name="presidentJury">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:presidentJury/tef:nom"/>
                    <xsl:text>,</xsl:text>
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:presidentJury/tef:prenom"/>
                </field>

                <!-- intervenant : directeur thèse -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:directeurThese">
                    <field name="intervenant">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:text>[</xsl:text>
                        <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                        <xsl:text>]</xsl:text>
                    </field>
    	          <field name="directeurThese">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
    	          </field>	
                    <field name="directeurThesePpn">
                        <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                    </field>
                </xsl:for-each>

                <!-- intervenant : rapporteurs -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:rapporteur">
                    <field name="intervenant">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:prenom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:text>[</xsl:text>
                        <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                        <xsl:text>]</xsl:text>
                    </field>
		    <field name="rapporteur">
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
                
                <field name="auteurEtCo">
                    <xsl:value-of select="//tef:auteur/tef:nom"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="//tef:auteur/tef:prenom"/>                    
                    <xsl:for-each select="//tefextension:coAuteur">
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="tef:prenom"/>                        
                    </xsl:for-each>
                </field>

                <!-- établissement de soutenance -->                
                <field name="etabSoutenance">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[1]/tef:nom"/>
                </field>                
                <field name="etabSoutenancePpn">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[1]/tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                </field>

                <!-- code etab -->

                <!--field name="codeEtab">
                    <xsl:value-of select="substring(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dc:identifier[@xsi:type='tef:NNT'],5,4)"/>
                </field-->
                <!-- co-tutelle -->

                <!-- co-tutelles -->
				<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:thesis.degree/tef:thesis.degree.grantor[position()!='1']">
					<field name="coTutelle">
						<xsl:value-of select="tef:nom"/>
					</field>
				</xsl:for-each>

                <!-- ecoles doctorales -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:ecoleDoctorale">
                    <field name="ecoleDoctorale">
                        <xsl:value-of select="tef:nom"/>
                    </field>
                    <field name="ecoleDoctoralePpn">
                        <xsl:value-of select="tef:autoriteExterne[@autoriteSource='Sudoc']"/>
                    </field>                    
                </xsl:for-each>

                <!-- partenaires de recherche -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche/tef:nom">
                    <field name="partRecherche">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- types partenaire de recherche -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche">
                    <field name="typePartRecherche">
                        <xsl:value-of select="@type"/>
                    </field>
                </xsl:for-each>

				<!-- partenaire de recherche par type -->
				
		<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche[@type='laboratoire']">
                    <field name="partenaireLaboratoire">
                        <xsl:value-of select="tef:nom"/>                        
                    </field>
                </xsl:for-each>
				                                                                                				
  		<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche[@type='equipeRecherche']">
	            <field name="partenaireEquipeRecherche">
	                <xsl:value-of select="tef:nom"/>                        
	            </field>
	        </xsl:for-each>
			                                                                                				
		<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche[@type='entreprise']">
	            <field name="partenaireEntreprise">
	                <xsl:value-of select="tef:nom"/>                        
	            </field>
	        </xsl:for-each>
				                                                                                				
		<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche[@type='fondation']">
		    <field name="partenaireFondation">
		        <xsl:value-of select="tef:nom"/>                        
		    </field>
	        </xsl:for-each>
				                                                                                				
		<xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche[@type='autreType']">
		    <field name="partenaireAutreType">
		        <xsl:value-of select="@autreType"/>, <xsl:value-of select="tef:nom"/>                        
		    </field>
	        </xsl:for-each>

                <!-- partenaire -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:partenaireRecherche">
                    <field name="partenaire">
                        <xsl:value-of select="tef:nom"/>
                        <xsl:text>,</xsl:text>
                        <xsl:value-of select="@type"/>
                    </field>
                </xsl:for-each>

                <!-- editeurs -->

                <xsl:for-each select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:edition/tef:editeur/tef:nom">
                    <field name="editeur">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- date de soutenance -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dcterms:dateAccepted/text()!=''">
                <field name="dateSoutenance_dt">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dcterms:dateAccepted"/>
                    <xsl:text>T23:59:59Z</xsl:text>
                </field>
                </xsl:if>

		<!-- date de soutenance prevue -->
                <xsl:if test="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue/text()!=''">
                	<field name="dateSoutenancePrevue_dt">
                        	<xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue"/>
                                <xsl:if test="string-length(/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:soutenancePrevue/suj:datePrevue/text())!=20">
                                	<xsl:text>T23:59:59Z</xsl:text>
                                </xsl:if>
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

                <!-- sujet rameau élément d'entrée -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau/tef:vedetteRameauNomCommun/tef:elementdEntree">
                    <field name="sujetRameauElemEntree">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- sujet rameau subdivision -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau/tef:vedetteRameauNomCommun/tef:subdivision">
                    <field name="sujetRameauSubDiv">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

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

                <!-- coverage -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:coverage">
                    <field name="coverage">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- spatial -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dcterms:spatial">
                    <field name="spatial">
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

                <!-- langues de la thèse -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:language">
                    <field name="langueThese">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- TOC -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dcterms:tableOfContents">
                    <field name="toc">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- type de doc -->

                <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:thesisRecord/dc:type">
                    <field name="typeDeDoc">
                        <xsl:value-of select="text()"/>
                    </field>
                </xsl:for-each>

                <!-- nnt -->

                <field name="nnt">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/dc:identifier[@xsi:type='tef:NNT']"/>
                </field>

    	    <!-- these sur trvx -->
    	    <field name="theseSurTravaux">
                    <xsl:value-of select="/mets:mets/mets:amdSec[1]/mets:techMD[1]/mets:mdWrap[1]/mets:xmlData[1]/tef:thesisAdmin[1]/tef:theseSurTravaux[1]"/>
    	    </field>
                
                <!--Format du document d'archivage -->
                <field name="formatArchivage">
                    <xsl:value-of select="/mets:mets/mets:dmdSec[@ID=//mets:div[@TYPE='EDITION'][mets:fptr/@FILEID=//mets:fileGrp[@USE ='archive' or @USE='archive_et_diffusion']/@ID]/@DMDID]/mets:mdWrap[@OTHERMDTYPE='tef_desc_edition']/mets:xmlData/tef:edition/dcterms:medium"/>                   
                </field>
                
                <!--Impression autorisee -->
                <field name="impressionAutorisee">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:rightsMD/mets:mdWrap[@OTHERMDTYPE='tef_droits_etablissement_these']/mets:xmlData/metsRights:RightsDeclarationMD/metsRights:Context[1]/metsRights:Permissions[1]/@PRINT"/>                   
                </field>
                
                <!--Reutilisation autorisee -->
                <field name="reutilisationAutorisee">
                    <xsl:value-of select="/mets:mets/mets:amdSec/mets:rightsMD/mets:mdWrap[@OTHERMDTYPE='tef_droits_etablissement_these']/mets:xmlData/metsRights:RightsDeclarationMD/metsRights:Context[1]/metsRights:Permissions[1]/@COPY"/>                   
                </field>
                
                
		<!-- Données de gestion -->

                <xsl:call-template name="traiterStarGestion"/>

            </doc>
        </add>
    </xsl:template>
    
    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzàâäéèêëîïôöùûüç'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZAAAEEEEIIOOUUUC'" />
    
    <xsl:template name="UPPER">
        <xsl:param name="text"/>
        <xsl:value-of select="translate(translate(translate($text, $smallcase, $uppercase),'-',''),' ','')"/>
    </xsl:template>
    
    <xsl:template name="LOWER">
        <xsl:param name="text"/>
        <xsl:value-of select="translate($text, $uppercase, $smallcase)"/>
    </xsl:template> 
    
</xsl:stylesheet>
