<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mets="http://www.loc.gov/METS/" xmlns:metsRights="http://cosimo.stanford.edu/sdr/metsrights/" xmlns:tef="http://www.abes.fr/abes/documents/tef"
    xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    version="1.0">
    <xsl:output indent="yes"/>
    
    <xsl:template name="traiterStarGestion">

        <field name="SGcodeEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/attribute::codeEtab"/>
        </field>
        <field name="SGID_THESE">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/attribute::ID_THESE"/>
        </field>
        <field name="SGetat">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/attribute::etat"/>
        </field>
        <field name="SGtypeEnt">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::type"/>
        </field>
        <field name="SGdateEnt">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::date"/>
        </field>
        <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::date)>18">
      		<field name="SGdateEnt_dt">
      			<xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::date"/>
      			<xsl:if test="not(contains(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::date,'Z'))">
      				<xsl:text>Z</xsl:text>
      			</xsl:if>            
      		</field>
        </xsl:if>
        <field name="SGagentImpEnt">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::agentImport"/>
        </field>
        <field name="SGcomplImpEnt">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::completudeImport"/>
        </field>
	 <field name="SGidSourceEnt">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree/attribute::idSource"/>
        </field>
        <field name="SGdateMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::date"/>
        </field>
        <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::date)>18">
      		<field name="SGdateMaj_dt">      		   
                <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::date"/>
                <xsl:if test="not(contains(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::date,'Z'))">
                    <xsl:text>Z</xsl:text>
                </xsl:if>       		        	    		   
      		</field>
        </xsl:if>
        <field name="SGimportMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::import"/>
        </field>
        <field name="SGSCOLMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::SCOL"/>
        </field>
        <field name="SGBIBLMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::BIBL"/>
        </field>
        <field name="SGFICHMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::FICH"/>
        </field>
        <field name="SGVALIDMaj">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/maj/attribute::VALID"/>
        </field>
        <field name="SGdateFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::date"/>
        </field>
        <field name="SGindicFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::indicFacile"/>
        </field>
		<field name="SGtraceFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::trace"/>
        </field>
        <field name="SGimportFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::import"/>
        </field>
        <field name="SGSCOLFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::SCOL"/>
        </field>
        <field name="SGBIBLFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::BIBL"/>
        </field>
        <field name="SGFICHFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::FICH"/>
        </field>
        <field name="SGVALIDFac">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/facile/attribute::VALID"/>
        </field>
        <field name="SGindicCtrlUrl">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/ctrlUrl/attribute::indicCtrlUrl"/>
        </field>
        <field name="SGerreurUrl">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/ctrlUrl/attribute::erreurUrl"/>
        </field>
        <field name="SGcptIndicUrlKO">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/ctrlUrl/attribute::compteIndicCtrlUrlKO"/>
        </field>
        <field name="SGdateCtr">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/ctrlUrl/attribute::date"/>
        </field>
        <field name="SGindicRemArch">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/remonteeArchive/attribute::indicRemonteeArchive"/>
        </field>
        <field name="SGdateRemArch">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/remonteeArchive/attribute::date"/>
        </field>
		<field name="SGtraceRemArch">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/remonteeArchive/attribute::trace"/>
        </field>		
        <field name="SGindicPur">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/purge/attribute::indicPurge"/>
        </field>
        <field name="SGdatePur">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/purge/attribute::date"/>
        </field>
        <field name="SGindicInval">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/invalidation/attribute::indicInvalidation"/>
        </field>
        <field name="SGdateInval">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/invalidation/attribute::date"/>
        </field>
        <field name="SGnumPACCines">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines/attribute::numeroPAC"/>
        </field>
        <field name="SGdateCines">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines/attribute::dateCines"/>
        </field>
        <field name="SGindicCines">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines/attribute::indicCines"/>
        </field>
		<field name="SGtraceCines">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines/attribute::trace"/>
        </field>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/RCR">
            <field name="SGRCRSudoc">
                <xsl:value-of select="attribute::code"/>
            </field>
        </xsl:for-each>
        <field name="SGPPNSudoc">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/attribute::PPN"/>
        </field>
        <field name="SGdateSudoc">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/attribute::dateSudoc"/>
        </field>
        <field name="SGindicSudoc">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/attribute::indicSudoc"/>
        </field>
        <field name="SGmajSudoc">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/attribute::majSudoc"/>
        </field>
		<field name="SGtraceSudoc">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/sudoc/attribute::trace"/>
        </field>		
        <field name="SGurlPerenne">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/attribute::urlPerenne"/>
        </field>
        <field name="SGconformPol">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/attribute::conformitePolDiffusion"/>
        </field>
        <field name="SGtypeDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/attribute::typeDiffusion"/>
        </field>
        <field name="SGrestrTmpTypeDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/attribute::restrictionTemporelleType"/>
        </field>
        <field name="SGrestrTmpFinDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/attribute::restrictionTemporelleFin"/>
        </field>
        <field name="SGabesDiffPolEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::abesDiffuseurPolEtablissement"/>
        </field>
        <field name="SGFichEtabDifAbes">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::FichEtabDiffAbesIntranet"/>
        </field>
        <field name="SGindicAbesDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::indicAbesDiffuseur"/>
        </field>
        <field name="SGdateAbesDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::dateAbesDiffuseur"/>
        </field>
        <field name="SGmajAbesDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::majAbesDiffuseur"/>
        </field>
        <field name="SGurlAbesDif">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/attribute::urlAbesDiffuseur"/>
        </field>        
        <field name="SGccsdDifPolEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::ccsdDiffuseurPolEtablissement"/>
        </field>
        <field name="SGpastelCcsd">            
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::pastel"/>                         
        </field>
        <field name="SGindicCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::indicCcsd"/>
        </field>
        <field name="SGidentifiantCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::identifiantCcsd"/>
        </field>
        <field name="SGpwdCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::pwdCcsd"/>
        </field>
        <field name="SGurlCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::urlCcsd"/>
        </field>
        <field name="SGnumVersionCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::numVersion"/>
        </field>        
        <field name="SGdateCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::dateCcsd"/>
        </field>
        <field name="SGmajCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::majCcsd"/>
        </field>
		<field name="SGtraceCcsd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/attribute::trace"/>
        </field>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/laboCcsd">
            <field name="SGcodeLaboCcsd">
                <xsl:value-of select="attribute::codeLaboCcsd"/>
            </field>
        </xsl:for-each>
	<xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/ccsd/laboCcsd">
            <field name="SGlabo">
		<xsl:value-of select="attribute::libLaboCcsd"/>
		<xsl:text>,</xsl:text>
                <xsl:text>[</xsl:text>
		<xsl:value-of select="attribute::codeLaboCcsd"/>
                <xsl:text>]</xsl:text>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/autresEtabDiffuseurs/autreEtabDiffuseur">
            <field name="SGcodeAutEtabDif">
                <xsl:value-of select="attribute::codeAutreEtabDiffuseur"/>
            </field>
        </xsl:for-each>
        <field name="SGmajOai">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/oai/attribute::majOai"/>
        </field>
        <field name="SGdateOai">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/oai/attribute::dateOai"/>
        </field>
        <field name="SGindicOai">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/oai/attribute::indicOai"/>
        </field>
        <field name="SGetatWF">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/attribute::etatWF"/>
        </field>
        <field name="SGnbCom">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/attribute::nbCommentaires"/>
        </field>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire">
            <field name="SGdateCom">
                <xsl:value-of select="attribute::date"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire/expediteur">
            <field name="SGDOCTExpCom">
                <xsl:value-of select="attribute::DOCT"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire/expediteur">
            <field name="SGSCOLExpCom">
                <xsl:value-of select="attribute::SCOL"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire/expediteur">
            <field name="SGBIBLExpCom">
                <xsl:value-of select="attribute::BIBL"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire/expediteur">
            <field name="SGFICHExpCom">
                <xsl:value-of select="attribute::FICH"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire/expediteur">
            <field name="SGVALIDExpCom">
                <xsl:value-of select="attribute::VALID"/>
            </field>
        </xsl:for-each>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/commentaires/commentaire">
            <field name="SGDestinataireCom">
                <xsl:value-of select="attribute::destinataire"/>
            </field>
        </xsl:for-each>
        <field name="SGintFichEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/DOCT/attribute::interventionFicheEtablissement"/>
        </field>
        <field name="SGetatDOCT">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/DOCT/attribute::etatDOCT"/>
        </field>
        <field name="SGmajDOCT">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/DOCT/attribute::majDOCT"/>
        </field>
        <field name="SGetatMDSco">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/SCOL/attribute::etatMD"/>
        </field>
        <field name="SGmajMDSco">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/SCOL/attribute::majMD"/>
        </field>
        <field name="SGnbNotSco">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/SCOL/notes/attribute::nbNotes"/>
        </field>
        <field name="SGetatMDBib">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/BIBL/attribute::etatMD"/>
        </field>
        <field name="SGmajMDBib">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/BIBL/attribute::majMD"/>
        </field>
        <field name="SGindicThesTravBib">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/BIBL/attribute::indicTheseTravaux"/>
        </field>
        <field name="SGnbNotBib">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/BIBL/notes/attribute::nbNotes"/>
        </field>
        <field name="SGetatMDFic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/FICH/attribute::etatMD"/>
        </field>
        <field name="SGmajMDFic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/FICH/attribute::majMD"/>
        </field>
        <field name="SGalertThesTravFic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/FICH/attribute::alerteTheseTravaux"/>
        </field>
        <field name="SGnbNotFic">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/FICH/notes/attribute::nbNotes"/>
        </field>
        <field name="SGvalidIndepVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/attribute::validIndependant"/>
        </field>
        <field name="SGetatVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/attribute::etatVALID"/>
        </field>
        <field name="SGmajVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/attribute::majVALID"/>
        </field>
        <field name="SGnbNotVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/notes/attribute::nbNotes"/>
        </field>
        <field name="SGnbRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/attribute::nbRefus"/>
        </field>
        <field name="SGdateRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/refus/attribute::date"/>
        </field>
        <field name="SGcontenuRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/refus/attribute::contenu"/>
        </field>
        <field name="SGSCOLRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/refus/attribute::SCOL"/>
        </field>
        <field name="SGFICHRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/refus/attribute::FICH"/>
        </field>
        <field name="SGBIBLRefVal">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID/refusValidationIntermediaire/refus/attribute::BIBL"/>
        </field>
	<field name="SGetabProd">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/attribute::enProd"/>
        </field>
	<field name="SGetabDiffPolEtab">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/etabDiffuseur/attribute::etabDiffuseurPolEtablissement"/>
	</field>
        <xsl:for-each select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/etabDiffuseur/urlEtabDiffuseur">
            <field name="SGetabUrl">
                <xsl:value-of select="text()"/>
            </field>
        </xsl:for-each>
        <field name="SGscenario">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/attribute::scenario"/>
        </field>

        <!-- ACT 04/11/15 -->
        <field name="SGembargoFinDif">           
            <xsl:choose>
                <xsl:when test="//star_gestion/traitements/sorties/diffusion/@embargoFin!='' ">
                    <xsl:value-of select="//star_gestion/traitements/sorties/diffusion/@embargoFin"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="chaineEmb">
                        <xsl:value-of select="//mets:mdWrap[@OTHERMDTYPE='tef_droits_auteur_these']//metsRights:Constraints[@CONSTRAINTTYPE='TIME']/metsRights:ConstraintDescription/text()"/>
                    </xsl:variable>
                    <xsl:if test="contains($chaineEmb,'restriction') and string-length($chaineEmb)=33" >
                        <xsl:value-of select="substring($chaineEmb,24,10)"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>       
        </field>				
        <field name="SGconfidentialiteFinDif">
            <xsl:choose>
                <xsl:when test="//star_gestion/traitements/sorties/diffusion/@confidentialiteFin!='' ">
                    <xsl:value-of select="//star_gestion/traitements/sorties/diffusion/@confidentialiteFin"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="chaineConf">
                        <xsl:value-of select="//mets:mdWrap[@OTHERMDTYPE='tef_droits_etablissement_these']//metsRights:Constraints[@CONSTRAINTTYPE='TIME']/metsRights:ConstraintDescription/text()"/>
                    </xsl:variable>
                    <xsl:if test="contains($chaineConf,'confidentialité') and string-length($chaineConf)=37" >
                        <xsl:value-of select="substring($chaineConf,28,10)"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>                    
        </field>       
		
		
		
	<xsl:for-each select="//star_gestion//urlIntraEtabDiffuseur">
		<xsl:if test="text()!=''">
			<field name="SGurlIntranet">
				<xsl:value-of select="text()"/>
			</field>    
		</xsl:if>            
	</xsl:for-each>
	<xsl:for-each select="//dc:identifier[@xsi:type='tef:URI_intranetEmbargo']">
		<xsl:if test="text()!=''">
			<field name="SGurlIntranet">
				<xsl:value-of select="text()"/>
			</field>    
		</xsl:if>            
	</xsl:for-each>
 
        <!-- OMZ 12/06/13 ajout MD balise step -->
        <field name="SGstepDate">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::date"/>
        </field>
        
        <xsl:if test="string-length(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::date)>18">
        <field name="SGstepDate_dt">      		   
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::date"/>
            <xsl:if test="not(contains(/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::date,'Z'))">
                <xsl:text>Z</xsl:text>
            </xsl:if>       		        	    		   
        </field>
         </xsl:if>
        
        <field name="SGstepAgentImport">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::agentImport"/>
        </field>
        
        <field name="SGstepId">
            <xsl:value-of select="/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/step/attribute::idStep"/>
        </field>
        
    </xsl:template>

</xsl:stylesheet>
