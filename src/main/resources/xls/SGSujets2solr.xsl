<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mets="http://www.loc.gov/METS/" xmlns:tef="http://www.abes.fr/abes/documents/tef"
    xmlns:dcs="http://purl.org/dc/elements/1.1/" xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    version="1.0">
    <xsl:output indent="yes"/>
    
    <xsl:template name="traiterSujetsGestion">
        
        <!-- <step_gestion> -->
        <field name="SGcodeEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::codeEtab"/>
        </field>
        <field name="SGID_SUJET">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::ID_SUJET"/>
        </field>
        <field name="SGstepEtat">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::stepEtat"/>
        </field>

        <field name="SGenProdStep">
            <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::enProdStar">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::enProdStar"/>
            </xsl:if>  
            <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::enProdStep">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/attribute::enProdStep"/>
            </xsl:if>
        </field>
        
        <!-- <entree> -->
        <field name="SGentreeType">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::type"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::date!=''">
            <field name="SGentreeDate_dt">     
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::date"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::date)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>
            </field>
            <field name="SGentreeDate">     
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::date"/>
            </field>
        </xsl:if>
        
        <field name="SGentreeAgentImport">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::agentImport"/>
        </field>
        <field name="SGentreeCompImport">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::completudeImport"/>
        </field>
        <field name="SGentreeIdSource">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree/attribute::idSource"/>
        </field>
        
        <!-- <maj> -->
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::date!=''">
            <field name="SGmajDate_dt">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::date"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::date)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>
            </field>
        </xsl:if>
        <field name="SGmajDate">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::date"/>
        </field>
        
        <field name="SGmajImport">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::import"/>
        </field>       
        <field name="SGderniereIntervention">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/maj/attribute::derniereIntervention"/>
        </field>
        

        <!-- <sorties nnt> -->      
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::dateNnt!=''">
            <field name="SGnntDate_dt">           
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::dateNnt"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::dateNnt)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>
            </field>
        </xsl:if>    
        <field name="SGnntDate">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::dateNnt"/>
        </field>
        
        <field name="SGnntSource">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::sourceNnt"/>
        </field>
        <field name="SGnntIndic">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::indicNnt"/>
        </field>
        <field name="SGnntTrace">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/attribute::indicTrace"/>
        </field>
	 
	 <field name="SGnnt">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt/text()"/>
        </field>

         
        <!-- <sorties sudoc> -->
        <field name="SGsudocPPN">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::PPN"/>
        </field>      
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::dateSudoc!=''">
            <field name="SGsudocDate_dt">           
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::dateSudoc"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::dateSudoc)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>                   
            </field>
        </xsl:if>    
        <field name="SGsudocDate">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::dateSudoc"/>
        </field>
        
        <field name="SGsudocIndic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::indicSudoc"/>
        </field>
        <field name="SGsudocTrace">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/sudoc/attribute::trace"/>
        </field>
        
        <!-- <sorties star> -->
        <field name="SGstarExportrActif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::exportStarActif"/>
        </field>
        <field name="SGstarID_THESE">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::ID_THESE"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateReponseStar!=''">
            <field name="SGstarDateReponse_dt">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateReponseStar"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateReponseStar)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>   
            </field>     
        </xsl:if>
        
        <field name="SGstarDateReponse">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateReponseStar"/>
        </field>  
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateExportVersStar!=''">
            <field name="SGstarDateExport_dt">  
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateExportVersStar"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateExportVersStar)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>
            </field>
        </xsl:if>   
        <field name="SGstarDateExport">  
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::dateExportVersStar"/>
        </field>
        
        <field name="SGstarIndic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::indicStar"/>
        </field>
        <field name="SGstarTrace">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/star/attribute::trace"/>
        </field>
        
        <!-- <sorties attestation> -->
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateDemandeAttestation!=''">
            <field name="SGdateDemandeAttestation_dt">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateDemandeAttestation"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateDemandeAttestation)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>         
                 </xsl:if>
            </field>
        </xsl:if>
        
        <field name="SGdateDemandeAttestation">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateDemandeAttestation"/>    
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateEditionAttestation!=''">
            <field name="SGdateEditionAttestation_dt">    
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateEditionAttestation"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateEditionAttestation)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                </xsl:if>
            </field>
        </xsl:if>
        <field name="SGdateEditionAttestation">    
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::dateEditionAttestation"/>
        </field>
        
        <field name="SGattestationEditee">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/attestation/attribute::attestationEditee"/>
        </field>

        <!-- <sorties diffusion> -->
        <field name="SGurlPerenne">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::urlPerenne"/>
        </field>
        <field name="SGinvisible">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::invisible"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateInvisible!=''">
            <field name="SGdateInvisible_dt">           
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateInvisible"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateInvisible)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                </xsl:if>
            </field>
        </xsl:if>
        <field name="SGdateInvisible">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateInvisible"/>
        </field> 
        
        <field name="SGindicDiffusion">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::indicDiffusion"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateDiffusion!=''">
            <field name="SGdateDiffusion_dt">           
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateDiffusion"/>
                <!--xsl:text>T23:59:59Z</xsl:text-->
            </field>
        </xsl:if> 
        <field name="SGdateDiffusion">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::dateDiffusion"/>
        </field>
        
        <field name="SGtraceDiffusion">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/diffusion/attribute::trace"/>
        </field>
        
        <!-- <transfert> -->
        <field name="SGtransfertEnCours">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::transfertEnCours"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDemandeTransfert!=''">
            <field name="SGdateDemandeTransfert_dt">           
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDemandeTransfert"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDemandeTransfert)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                 </xsl:if>
            </field>
        </xsl:if>
        <field name="SGdateDemandeTransfert">           
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDemandeTransfert"/>
        </field>
        
        <xsl:if test="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDepartEtabOrigine!=''">
            <field name="SGdateDepartEtabOrigine_dt">
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDepartEtabOrigine"/>
                <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDepartEtabOrigine)!=20">
                    <xsl:text>T23:59:59Z</xsl:text>
                </xsl:if>
            </field>
        </xsl:if>
        <field name="SGdateDepartEtabOrigine">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::dateDepartEtabOrigine"/>
        </field>
        
        <field name="SGtransfertEtabDepart">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::transfertEtabDepart"/>
        </field>
        <field name="SGtransfertEtabArrivee">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::transfertEtabArrivee"/>
        </field>
        <field name="SGaSubiUnTransfert">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/transfert/attribute::aSubiUnTransfert"/>
        </field>

    </xsl:template>
    
</xsl:stylesheet>
