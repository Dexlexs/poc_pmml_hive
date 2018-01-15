package com.poc.pmml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;

import java.util.ArrayList;
import java.util.List;


@Description(
        name = "DecisionTreeUDF"
)
public class DecisionTreeUDF extends GenericUDF {
    private static final Log LOG = LogFactory.getLog(DecisionTreeUDF.class.getName());
    PMMLUtil _evaluator;
    List<PrimitiveObjectInspector> inputOI;


    private ObjectInspector[] _inputObjectInspector = null;


    private GenericUDFUtils.ReturnObjectInspectorResolver
            _returnObjectInspectorResolver = null;


    private String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<PMML version=\"4.3\" xmlns=\"http://www.dmg.org/PMML-4_3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.dmg.org/PMML-4_3 http://www.dmg.org/pmml/v4-3/pmml-4-3.xsd\">" +
            " <Header copyright=\"Copyright (c) 2018 rsmith\" description=\"RPart Decision Tree Model\">" +
            "  <Extension name=\"user\" value=\"rsmith\" extender=\"Rattle/PMML\"/>" +
            "  <Application name=\"Rattle/PMML\" version=\"1.4\"/>" +
            "  <Timestamp>2018-01-08 15:37:08</Timestamp>" +
            " </Header>" +
            " <DataDictionary numberOfFields=\"4\">" +
            "  <DataField name=\"Kyphosis\" optype=\"categorical\" dataType=\"string\">" +
            "   <Value value=\"absent\"/>" +
            "   <Value value=\"present\"/>" +
            "  </DataField>" +
            "  <DataField name=\"Age\" optype=\"continuous\" dataType=\"double\"/>" +
            "  <DataField name=\"Number\" optype=\"continuous\" dataType=\"double\"/>" +
            "  <DataField name=\"Start\" optype=\"continuous\" dataType=\"double\"/>" +
            " </DataDictionary>" +
            " <TreeModel modelName=\"RPart_Model\" functionName=\"classification\" algorithmName=\"rpart\" splitCharacteristic=\"binarySplit\" missingValueStrategy=\"defaultChild\" noTrueChildStrategy=\"returnLastPrediction\">" +
            "  <MiningSchema>" +
            "   <MiningField name=\"Kyphosis\" usageType=\"predicted\"/>" +
            "   <MiningField name=\"Age\" usageType=\"active\"/>" +
            "   <MiningField name=\"Number\" usageType=\"active\"/>" +
            "   <MiningField name=\"Start\" usageType=\"active\"/>" +
            "  </MiningSchema>" +
            "  <Output>" +
            "   <OutputField name=\"Predicted_Kyphosis\" optype=\"categorical\" dataType=\"string\" feature=\"predictedValue\"/>" +
            "   <OutputField name=\"Probability_absent\" optype=\"continuous\" dataType=\"double\" feature=\"probability\" value=\"absent\"/>" +
            "   <OutputField name=\"Probability_present\" optype=\"continuous\" dataType=\"double\" feature=\"probability\" value=\"present\"/>" +
            "  </Output>" +
            "  <Node id=\"1\" score=\"absent\" recordCount=\"81\" defaultChild=\"2\">" +
            "   <True/>" +
            "   <ScoreDistribution value=\"absent\" recordCount=\"64\" confidence=\"0.790123456790123\"/>" +
            "   <ScoreDistribution value=\"present\" recordCount=\"17\" confidence=\"0.209876543209877\"/>" +
            "   <Node id=\"2\" score=\"absent\" recordCount=\"62\" defaultChild=\"4\">" +
            "    <CompoundPredicate booleanOperator=\"surrogate\">" +
            "     <SimplePredicate field=\"Start\" operator=\"greaterOrEqual\" value=\"8.5\"/>" +
            "     <SimplePredicate field=\"Number\" operator=\"lessThan\" value=\"6.5\"/>" +
            "    </CompoundPredicate>" +
            "    <ScoreDistribution value=\"absent\" recordCount=\"56\" confidence=\"0.903225806451613\"/>" +
            "    <ScoreDistribution value=\"present\" recordCount=\"6\" confidence=\"0.0967741935483871\"/>" +
            "    <Node id=\"4\" score=\"absent\" recordCount=\"29\">" +
            "     <CompoundPredicate booleanOperator=\"surrogate\">" +
            "      <SimplePredicate field=\"Start\" operator=\"greaterOrEqual\" value=\"14.5\"/>" +
            "      <SimplePredicate field=\"Number\" operator=\"lessThan\" value=\"3.5\"/>" +
            "      <SimplePredicate field=\"Age\" operator=\"lessThan\" value=\"16\"/>" +
            "     </CompoundPredicate>" +
            "     <ScoreDistribution value=\"absent\" recordCount=\"29\" confidence=\"1\"/>" +
            "     <ScoreDistribution value=\"present\" recordCount=\"0\" confidence=\"0\"/>" +
            "    </Node>" +
            "    <Node id=\"5\" score=\"absent\" recordCount=\"33\" defaultChild=\"10\">" +
            "     <CompoundPredicate booleanOperator=\"surrogate\">" +
            "      <SimplePredicate field=\"Start\" operator=\"lessThan\" value=\"14.5\"/>" +
            "      <SimplePredicate field=\"Number\" operator=\"greaterOrEqual\" value=\"3.5\"/>" +
            "      <SimplePredicate field=\"Age\" operator=\"greaterOrEqual\" value=\"16\"/>" +
            "     </CompoundPredicate>" +
            "     <ScoreDistribution value=\"absent\" recordCount=\"27\" confidence=\"0.818181818181818\"/>" +
            "     <ScoreDistribution value=\"present\" recordCount=\"6\" confidence=\"0.181818181818182\"/>" +
            "     <Node id=\"10\" score=\"absent\" recordCount=\"12\">" +
            "      <CompoundPredicate booleanOperator=\"surrogate\">" +
            "       <SimplePredicate field=\"Age\" operator=\"lessThan\" value=\"55\"/>" +
            "       <SimplePredicate field=\"Start\" operator=\"lessThan\" value=\"9.5\"/>" +
            "       <SimplePredicate field=\"Number\" operator=\"greaterOrEqual\" value=\"5.5\"/>" +
            "      </CompoundPredicate>" +
            "      <ScoreDistribution value=\"absent\" recordCount=\"12\" confidence=\"1\"/>" +
            "      <ScoreDistribution value=\"present\" recordCount=\"0\" confidence=\"0\"/>" +
            "     </Node>" +
            "     <Node id=\"11\" score=\"absent\" recordCount=\"21\" defaultChild=\"22\">" +
            "      <CompoundPredicate booleanOperator=\"surrogate\">" +
            "       <SimplePredicate field=\"Age\" operator=\"greaterOrEqual\" value=\"55\"/>" +
            "       <SimplePredicate field=\"Start\" operator=\"greaterOrEqual\" value=\"9.5\"/>" +
            "       <SimplePredicate field=\"Number\" operator=\"lessThan\" value=\"5.5\"/>" +
            "      </CompoundPredicate>" +
            "      <ScoreDistribution value=\"absent\" recordCount=\"15\" confidence=\"0.714285714285714\"/>" +
            "      <ScoreDistribution value=\"present\" recordCount=\"6\" confidence=\"0.285714285714286\"/>" +
            "      <Node id=\"22\" score=\"absent\" recordCount=\"14\">" +
            "       <SimplePredicate field=\"Age\" operator=\"greaterOrEqual\" value=\"111\"/>" +
            "       <ScoreDistribution value=\"absent\" recordCount=\"12\" confidence=\"0.857142857142857\"/>" +
            "       <ScoreDistribution value=\"present\" recordCount=\"2\" confidence=\"0.142857142857143\"/>" +
            "      </Node>" +
            "      <Node id=\"23\" score=\"present\" recordCount=\"7\">" +
            "       <SimplePredicate field=\"Age\" operator=\"lessThan\" value=\"111\"/>" +
            "       <ScoreDistribution value=\"absent\" recordCount=\"3\" confidence=\"0.428571428571429\"/>" +
            "       <ScoreDistribution value=\"present\" recordCount=\"4\" confidence=\"0.571428571428571\"/>" +
            "      </Node>" +
            "     </Node>" +
            "    </Node>" +
            "   </Node>" +
            "   <Node id=\"3\" score=\"present\" recordCount=\"19\">" +
            "    <CompoundPredicate booleanOperator=\"surrogate\">" +
            "     <SimplePredicate field=\"Start\" operator=\"lessThan\" value=\"8.5\"/>" +
            "     <SimplePredicate field=\"Number\" operator=\"greaterOrEqual\" value=\"6.5\"/>" +
            "    </CompoundPredicate>" +
            "    <ScoreDistribution value=\"absent\" recordCount=\"8\" confidence=\"0.421052631578947\"/>" +
            "    <ScoreDistribution value=\"present\" recordCount=\"11\" confidence=\"0.578947368421053\"/>" +
            "   </Node>" +
            "  </Node>" +
            " </TreeModel>" +
            "</PMML>";


    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        LOG.info("meulog init");



        LOG.info("meulog tentando evaluator");
        _evaluator = new PMMLUtil(xml,true);
        LOG.info("meulog evaluator" + _evaluator);
        _inputObjectInspector = arguments;
        for (ObjectInspector o :
                arguments) {
            inputOI.add((PrimitiveObjectInspector) o);
        }
        return _evaluator.Initialize(arguments);


    }


    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {


        return _evaluator.evaluateComplex(_inputObjectInspector, arguments);

    }


    @Override
    public String getDisplayString(String[] children) {
        return "DecisionTreePoc";
    }
}
