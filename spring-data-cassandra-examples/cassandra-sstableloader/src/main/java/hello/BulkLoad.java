package hello;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import org.apache.cassandra.config.Config;
import org.apache.cassandra.dht.Murmur3Partitioner;
import org.apache.cassandra.exceptions.InvalidRequestException;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;

/**
 * Usage: java bulkload.BulkLoad
 */
public class BulkLoad
{
    public static final String CSV_URL = "http://real-chart.finance.yahoo.com/table.csv?s=%s";

    /** Default output directory */
    public static final String DEFAULT_OUTPUT_DIR = "./data";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /** Keyspace name */
    public static final String KEYSPACE = "quote";
    /** Table name */
    public static final String TABLE = "historical_prices";

    /**
     * Schema for bulk loading table.
     * It is important not to forget adding keyspace name before table name,
     * otherwise CQLSSTableWriter throws exception.
     */
    public static final String SCHEMA = String.format("CREATE TABLE %s.%s (" +
                                                          "ticker ascii, " +
                                                          "date timestamp, " +
                                                          "open decimal, " +
                                                          "high decimal, " +
                                                          "low decimal, " +
                                                          "close decimal, " +
                                                          "volume bigint, " +
                                                          "adj_close decimal, " +
                                                          "PRIMARY KEY (ticker, date) " +
                                                      ") WITH CLUSTERING ORDER BY (date DESC)", KEYSPACE, TABLE);

    /**
     * INSERT statement to bulk load.
     * It is like prepared statement. You fill in place holder for each data.
     */
    public static final String INSERT_STMT = String.format("INSERT INTO %s.%s (" +
                                                               "ticker, date, open, high, low, close, volume, adj_close" +
                                                           ") VALUES (" +
                                                               "?, ?, ?, ?, ?, ?, ?, ?" +
                                                           ")", KEYSPACE, TABLE);

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("usage: java bulkload.BulkLoad <list of ticker symbols>");
            return;
        }

        // magic!
        Config.setClientMode(true);

        // Create output directory that has keyspace and table name in the path
        File outputDir = new File(DEFAULT_OUTPUT_DIR + File.separator + KEYSPACE + File.separator + TABLE);
        if (!outputDir.exists() && !outputDir.mkdirs())
        {
            throw new RuntimeException("Cannot create output directory: " + outputDir);
        }

        // Prepare SSTable writer
        CQLSSTableWriter.Builder builder = CQLSSTableWriter.builder();
        // set output directory
        builder.inDirectory(outputDir)
               // set target schema
               .forTable(SCHEMA)
               // set CQL statement to put data
               .using(INSERT_STMT)
               // set partitioner if needed
               // default is Murmur3Partitioner so set if you use different one.
               .withPartitioner(new Murmur3Partitioner());
        CQLSSTableWriter writer = builder.build();

        for (String ticker : args)
        {
            HttpURLConnection conn;
            try
            {
            	String u = String.format(CSV_URL, ticker);
            	System.out.println(u);
                URL url = new URL(u);
                conn = (HttpURLConnection) url.openConnection();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));            		
            		
                CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE)
            )
            {
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    System.out.println("Historical data not found for " + ticker);
                    continue;
                }

                csvReader.getHeader(true);

                // Write to SSTable while reading data
                List<String> line;
                while ((line = csvReader.read()) != null)
                {
                	System.out.println(line);
                    // We use Java types here based on
                    // http://www.datastax.com/drivers/java/2.0/com/datastax/driver/core/DataType.Name.html#asJavaClass%28%29
                    writer.addRow(ticker,
                                  DATE_FORMAT.parse(line.get(0)),
                                  new BigDecimal(line.get(1)),
                                  new BigDecimal(line.get(2)),
                                  new BigDecimal(line.get(3)),
                                  new BigDecimal(line.get(4)),
                                  Long.parseLong(line.get(5)),
                                  new BigDecimal(line.get(6)));
                }
            }
            catch (InvalidRequestException | ParseException | IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            writer.close();
        }
        catch (IOException ignore) {}
    }
}



//ORCL YHOO AAPL GOOG MSFT GABC GAIA GAIN GAINO GAINP GALE GALT GALTU GALTW GAME GARS GASS GBCI GBDC GBIM GBLI GBNK GBSN GCBC GCVRZ GDEF GENC GENE GEOS GERN GEVA GEVO GFED GFN GFNCP GFNSL GGAC GGACR GGACU GGACW GGAL GHDX GIFI GIGA GIGM GIII GILD GILT GK GKNT GLAD GLADO GLBS GLBZ GLDC GLDD GLDI GLMD GLNG GLPI GLRE GLRI GLUU GLYC GMAN GMCR GMLP GNBC GNCA GNCMA GNMA GNMK GNTX GNVC GOGL GOGO GOLD GOMO GOOD GOODN GOODO GOODP GOOG GOOGL GPIC GPOR GPRE GPRO GRBK GRFS GRID GRIF GRMN GROW GRPN GRVY GSBC GSIG GSIT GSM GSOL GSVC GT GTIM GTLS GTWN GTXI GUID GULF GULTU GURE GWGH GWPH GYRO PAAS PACB PACW PAGG PAHC PANL PARN PATI PATK PAYX PBCP PBCT PBHC PBIB PBIP PBMD PBPB PBSK PCAR PCBK PCCC PCH PCLN PCMI PCO PCOM PCRX PCTI PCTY PCYC PCYG PCYO PDBC PDCE PDCO PDEX PDFS PDII PDLI PDVW PEBK PEBO PEGA PEGI PEIX PENN PERF PERI PERY PESI PETS PETX PFBC PFBI PFBX PFIE PFIN PFIS PFLT PFMT PFPT PFSW PGC PGNX PGTI PHII PHIIK PHMD PICO PIH PINC PKBK PKOH PKT PLAB PLAY PLBC PLCE PLCM PLKI PLMT PLNR PLPC PLPM PLTM PLUG PLUS PLXS PMBC PMCS PMD PME PMFG PNBK PNFP PNNT PNQI PNRA PNRG PNTR PODD POOL POPE POWI POWL POZN PPBI PPC PPHM PPHMP PPSI PRAA PRAH PRAN PRCP PRFT PRFZ PRGN PRGNL PRGS PRGX PRIM PRKR PRMW PROV PRPH PRQR PRSC PRSN PRSS PRTA PRTK PRTO PRTS PRXI PRXL PSAU PSBH PSCC PSCD PSCE PSCF PSCH PSCI PSCM PSCT PSCU PSDV PSEC PSEM PSIX PSMT PSTB PSTI PSTR PSUN PTBI PTC PTCT PTEN PTIE PTLA PTNR PTNT PTRY PTSI PTX PULB PVTB PVTBP PWOD PWRD PWX PXLW PZZA KALU KANG KBAL KBIO KBSF KCAP KCLI KE KELYA KEQU KERX KEYW KFFB KFRC KFX KGJI KIN KINS KIRK KITE KLAC KLIC KLXI KMDA KNDI KONA KONE KOOL KOPN KOSS KPTI KRFT KRNT KRNY KTCC KTEC KTOS KTWO KUTV KVHI KWEB KYTH KZ
//BABY BAGR BAMM BANF BANFP BANR BANX BASI BBBY BBC BBCN BBEP BBEPP BBGI BBLU BBNK BBOX BBP  BBRG BBRY BBSI BCBP BCLI BCOM BCOR BCOV BCPC BCRX BDBD BDCV BDE  BDGE BDMS BDSI BEAT BEAV BEBE BECN BELFA BELFB BFIN BGCP BGFV BGMD BHAC BHACU BHBK BIB  BICK BIDU BIIB BIND BIOC BIOD BIOL BIOS BIS  BJRI BKCC BKEP BKEPP BKMU BKSC BKYF BLBD BLBDW BLCM BLDP BLDR BLFS BLIN BLKB BLMN BLMT BLPH BLRX BLUE BLVD BLVDU BLVDW BMRC BMRN BMTC BNCL BNCN BNDX BNFT BNSO BOBE BOCH BOFI BOKF BONA BONT BOOM BOSC BOTA BOTJ BPFH BPFHP BPFHW BPOP BPOPM BPOPN BPTH BRCD BRCM BRDR BREW BRID BRKL BRKR BRKS BRLI BSET BSF  BSFT BSPM BSQR BSRR BSTC BUR  BUSE BV  BVA  BVSN BWEN BWFG BWINA BWINB BWLD BYBK BYFC BYLK