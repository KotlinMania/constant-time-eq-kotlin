=== Deep Analysis: tmp/constant_time_eq/src (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/constant_time_eq/src (rust)
  Files: 1
  Total imports: 2

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 4
  Total imports: 9

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/constant_time_eq/src (1 files)
Target: src/commonMain/kotlin (4 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   1 files
Unmatched: 0 source, 2 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
lib                           constanttimeeq.Lib            0.56      0          12/12         0/0         1204.4    

=== Function and Symbol Details ===

lib -> constanttimeeq.Lib
  similarity: 0.56, priority: 1204.4, dependents: 0
  functions: 12/12 matched (target total: 26, required body score: 0.56)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  tests: 4/4 matched


=== Porting Quality Summary ===

Matched by exact header:          1 / 1
Matched by provenance fallback:   0 / 1
Matched by name:                  0 / 1
Total TODOs in target: 0
Total lint errors:    0
Stub files:           0

=== Big Picture ===

- Missing files: 0
- Incomplete ports (similarity < 60%): 1
- Stub files: 0
- Files missing functions: 0 (total deficit: 0 functions)
- Type definitions missing: 0
- Files missing tests: 0 (total deficit: 0 unported `#[test]` functions)
- Documentation coverage: 95 / 112 lines (85%)

Primary focus: improve incomplete ports (similarity < 60%)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
constanttimeeq.Lib            0.56       0.00       12/12         4/4       0     0     

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 1
Missing files: 0

Incomplete ports to complete:
  lib                            similarity=0.56 function_parity=12/12 dependents=0

=== Documentation Gaps ===

There is missing documentation that is hurting overall scoring.
Documentation coverage: 95 / 112 lines (85%)
Files with >20% doc gap: 0

No significant documentation gaps found.

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
