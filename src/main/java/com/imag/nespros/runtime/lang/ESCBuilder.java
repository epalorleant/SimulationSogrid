/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.lang;

import com.imag.nespros.runtime.base.Func1;
import com.imag.nespros.runtime.core.EPUnit;
import com.imag.nespros.runtime.core.EqualFilter;
import com.imag.nespros.runtime.core.FalseFilter;
import com.imag.nespros.runtime.core.FilterAgent;
import com.imag.nespros.runtime.core.GreatherOrEqualFilter;
import com.imag.nespros.runtime.core.GreatherThanFilter;
import com.imag.nespros.runtime.core.LessOrEqualThanFilter;
import com.imag.nespros.runtime.core.LessThanFilter;
import com.imag.nespros.runtime.core.LogicalAndFilter;
import com.imag.nespros.runtime.core.LogicalOrFilter;
import com.imag.nespros.runtime.core.NotEqualFilter;
import com.imag.nespros.runtime.core.TrueFilter;
import com.imag.nespros.runtime.core.WindowHandler;
import java.util.HashMap;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.apache.commons.collections15.map.FastHashMap;

/**
 *
 * @author epaln
 */
class ESCBuilder extends ESCExprBaseListener {

    ParseTreeProperty<EPUnit> operators;
    ParseTreeProperty<Func1> filters;
    ParseTreeProperty<WindowHandler> windows;
    HashMap<String, EPUnit> id2Operator;

    public ParseTreeProperty<EPUnit> getOperators() {
        return operators;
    }

    public void setOperators(ParseTreeProperty<EPUnit> operators) {
        this.operators = operators;
    }

    public ParseTreeProperty<Func1> getFilters() {
        return filters;
    }

    public void setFilters(ParseTreeProperty<Func1> filters) {
        this.filters = filters;
    }

    public ParseTreeProperty<WindowHandler> getWindows() {
        return windows;
    }

    public void setWindows(ParseTreeProperty<WindowHandler> windows) {
        this.windows = windows;
    }

    public HashMap<String, EPUnit> getId2Operator() {
        return id2Operator;
    }

    public void setId2Operator(HashMap<String, EPUnit> id2Operator) {
        this.id2Operator = id2Operator;
    }

    
    public ESCBuilder() {
        operators = new ParseTreeProperty<>();
        filters = new ParseTreeProperty<>();
        windows = new ParseTreeProperty<>();
        id2Operator = new FastHashMap<>();
    }

    @Override
    public void exitTerm(ESCExprParser.TermContext ctx) {
        if (ctx.ID() != null) { // a real terminal node
            String id = ctx.ID().getText();
            id = id.substring(1, id.length() - 1);
            switch (ctx.sign.getType()) {
                case ESCExprParser.GEQ:
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        GreatherOrEqualFilter geq = new GreatherOrEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        GreatherOrEqualFilter geq = new GreatherOrEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        GreatherOrEqualFilter geq = new GreatherOrEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
                case ESCExprParser.GT:
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        GreatherThanFilter geq = new GreatherThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        GreatherThanFilter geq = new GreatherThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        GreatherThanFilter geq = new GreatherThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
                case ESCExprParser.LT:
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        LessThanFilter geq = new LessThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        LessThanFilter geq = new LessThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        LessThanFilter geq = new LessThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
                case ESCExprParser.LEQ:
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        LessOrEqualThanFilter geq = new LessOrEqualThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        LessOrEqualThanFilter geq = new LessOrEqualThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        LessOrEqualThanFilter geq = new LessOrEqualThanFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
                case ESCExprParser.NEQ :
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        NotEqualFilter geq = new NotEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        NotEqualFilter geq = new NotEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        NotEqualFilter geq = new NotEqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
                default: // equal filter
                    if (ctx.DOUBLE() != null) {
                        double val = Double.parseDouble(ctx.DOUBLE().getText());
                        EqualFilter geq = new EqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.STRING() != null) {
                        String val = ctx.STRING().getText();
                        val = val.substring(1, val.length() - 1);
                        EqualFilter geq = new EqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    if (ctx.INT() != null) {
                        int val = Integer.parseInt(ctx.INT().getText());
                        EqualFilter geq = new EqualFilter(id, val);
                        filters.put(ctx, geq);
                        break;
                    }
                    break;
            }
            System.out.println(ctx.ID() + ctx.sign.getText() + ctx.val.getText());
            return;
        }

        if (ctx.TRUE() != null) { // True predicate
            TrueFilter f = new TrueFilter();
            filters.put(ctx, f);
            return;
        }
        if (ctx.FALSE() != null) { // True predicate
            FalseFilter f = new FalseFilter();
            filters.put(ctx, f);
            return;
        }

        if (ctx.predicate() != null) {// another predicate...

            System.out.println("the term refers to a predicate node");
            filters.put(ctx, filters.get(ctx.predicate()));
        }
    }

    @Override
    public void exitAnd_expression(ESCExprParser.And_expressionContext ctx) {
        if (ctx.term().size() == 2) {
            LogicalAndFilter f = new LogicalAndFilter();
            f.addPredicate(filters.get(ctx.term(0)));
            f.addPredicate(filters.get(ctx.term(1)));
            filters.put(ctx, f);
            System.out.println("And filter added with 2 filters");
        } else {
            filters.put(ctx, filters.get(ctx.term(0)));
            System.out.println("The and filter equals its only child");
        }
    }

    @Override
    public void exitOr_expression(ESCExprParser.Or_expressionContext ctx) {
        if (ctx.and_expression().size() == 2) { // or between two and expressions...
            LogicalOrFilter f = new LogicalOrFilter();
            f.addPredicate(filters.get(ctx.and_expression(0)));
            f.addPredicate(filters.get(ctx.and_expression(1)));
            filters.put(ctx, f);
            return;
        }
        filters.put(ctx, filters.get(ctx.and_expression(0)));
        return;
    }

    @Override
    public void exitPredicate(ESCExprParser.PredicateContext ctx) {
        filters.put(ctx, filters.get(ctx.or_expression()));
        return;
    }

    @Override
    public void exitOperator_type(ESCExprParser.Operator_typeContext ctx) {
        if (ctx.filterdef() != null) {
            operators.put(ctx, operators.get(ctx.filterdef()));
            return;
        }
        operators.put(ctx, operators.get(ctx.getChild(0)));
    }

    @Override
    public void exitFilterdef(ESCExprParser.FilterdefContext ctx) {
        FilterAgent filter = new FilterAgent("filter");
        filter.addFilter(filters.get(ctx.predicate()));
        operators.put(ctx, filter);
    }

    @Override
    public void exitOperator_def(ESCExprParser.Operator_defContext ctx) {
        id2Operator.put(ctx.ID().getText(), operators.get(ctx.operator_type()));
        //operators 
    }
    
    

}
