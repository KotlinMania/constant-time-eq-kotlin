# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 3/3 (100.0%)
- **Function parity:** 29/29 matched (target 45) — 100.0%
- **Class/type parity:** 0/0 matched (target 3) — N/A
- **Combined symbol parity:** 29/29 matched (target 48) — 100.0%
- **Average inline-code cosine:** 0.40 (function body across 2 matched files)
- **Average documentation cosine:** 0.00 (doc text across 2 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 2 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. tests.count_instructions

- **Target:** `constanttimeeq.CountInstructionsTest`
- **Similarity:** 0.64
- **Dependents:** 1
- **Priority Score:** 1001503.6
- **Functions:** 15/15 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 8/8 matched
- **Lint issues:** 2

### 2. constant_time_eq.lib

- **Target:** `constanttimeeq.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1210.0
- **Functions:** 12/12 matched (target 26)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 4/4 matched

### 3. benches.bench

- **Target:** `constanttimeeq.BenchTest`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 208.5
- **Functions:** 2/2 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

