package com.viching.generate.converter.mapper;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viching.generate.config.ICommentGenerator;
import com.viching.generate.converter.GenerateMethod;
import com.viching.generate.converter.InjectionOrder;
import com.viching.generate.elements.java.FullyQualifiedJavaType;
import com.viching.generate.elements.java.Interface;
import com.viching.generate.elements.java.JavaVisibility;
import com.viching.generate.elements.java.Method;
import com.viching.generate.elements.java.Parameter;
import com.viching.generate.source.DBTableJavaBean;

@Component
@InjectionOrder(13)
public class GenerateCountBySelective extends GenerateMethod {
	
	@Autowired
	private ICommentGenerator commentGenerator;

	@Override
	public void addInterfaceElements(Interface interfaze, DBTableJavaBean dbTable) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		
		FullyQualifiedJavaType parameterType;
        parameterType = new FullyQualifiedJavaType(dbTable.getBaseRecordType());
        importedTypes.add(parameterType);
        
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();
        method.setReturnType(returnType);

        method.setName(METHOD_COUNT_SELECTIVE);
        method.addParameter(new Parameter(parameterType, "record"));

        addMapperAnnotations(method, dbTable);
        
        commentGenerator.addGeneralMethodComment(method,
                dbTable);

        addExtraImports(interfaze, dbTable);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
	}
	
	@Override
    public void addMapperAnnotations(Method method, DBTableJavaBean dbTable) {

		FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
				dbTable.getProviderType());

		StringBuilder sb = new StringBuilder();
		sb.append("@SelectProvider(type=");
		sb.append(fqjt.getShortName());
		sb.append(".class, method=\"");
		sb.append(METHOD_COUNT_SELECTIVE);
		sb.append("\")");

		method.addAnnotation(sb.toString());
    }

    @Override
    public void addExtraImports(Interface interfaze, DBTableJavaBean dbTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider")); 

    }
}
