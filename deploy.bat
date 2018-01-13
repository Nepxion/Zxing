@echo on
@echo =============================================================
@echo $                                                           $
@echo $                       Nepxion Zxing                       $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2017                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Zxing
@color 0a

call mvn clean deploy -DskipTests -e -P release

pause